package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.tk2575.fantasysports.core.nfl.FantasyPlayerWeek;

import java.math.BigDecimal;
import java.util.*;

import static dev.tk2575.fantasysports.details.DetailsClient.getApplicationProperties;

public class MatchupService implements SleeperService {
    
    private final Gson gson = getGson();
    
    private final SleeperApiManager api = SleeperApiManager.getInstance();
    
    public List<FantasyPlayerWeek> getMatchups(String leagueId, 
                                               int week, 
                                               Map<Long, String> teamNamesByRosterId, 
                                               Map<String, String> playersAndPosById) 
        throws SleeperApiManager.SleeperApiServiceException {
        
        return getRawMatchups(leagueId, week).stream()
                .map(matchup -> getFantasyPlayerWeeks(week, matchup, teamNamesByRosterId, playersAndPosById))
                .flatMap(List::stream)
                .toList();
    }

    List<FantasyPlayerWeek> getFantasyPlayerWeeks(int week,
                                                  SleeperMatchup matchup, 
                                                  Map<Long, String> teamNamesByRosterId, 
                                                  Map<String, String> playersAndPosById) {
        if (matchup.getPlayers() == null || matchup.getPlayers().isEmpty()) {
            return Collections.emptyList();
        }
        
        List<FantasyPlayerWeek> results = new ArrayList<>();

        for (String playerId : matchup.getPlayers()) {
            String playerAndPos = playersAndPosById.get(playerId);
            String playerName = playerAndPos == null ? playerId : playerAndPos.split("---")[0];
            String position = playerAndPos == null ? "UNK" : playerAndPos.split("---")[1];

            var result = FantasyPlayerWeek.builder()
                    .player(playerName)
                    .fantasyTeamName(
                        teamNamesByRosterId.getOrDefault(
                            matchup.getRosterId(), 
                            matchup.getRosterId().toString())
                    ).week(week)
                    .position(position)
                    .started(matchup.getStarters().contains(playerId))
                    .points(matchup.getPlayersPoints().getOrDefault(playerId, BigDecimal.ZERO))
                    .build();
            
            results.add(result);
        }
        
        return results;
    }

    private List<SleeperMatchup> getRawMatchups(String leagueId, int week) throws SleeperApiManager.SleeperApiServiceException {
        String response = api.request(String.format("https://api.sleeper.app/v1/league/%s/matchups/%d", leagueId, week));
        var token = new TypeToken<ArrayList<SleeperMatchup>>() {}.getType();
        List<SleeperMatchup> matchups = gson.fromJson(response, token);
        return matchups == null ? Collections.emptyList() : matchups;
    }

    public static void main(String[] args) throws Exception {
        Properties appProps = getApplicationProperties();
        String leagueId = appProps.getProperty("sleeper.league-id");
        new MatchupService().getRawMatchups(leagueId, 1).forEach(System.out::println);
    }
    
}
