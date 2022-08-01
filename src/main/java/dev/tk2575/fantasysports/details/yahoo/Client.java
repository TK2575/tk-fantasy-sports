package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Client implements Runnable {

    private final YahooFantasyService service = YahooFantasyService.getInstance();
    private final Gson gson = YahooUtils.getGson();

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void run() {
        try {
            log.info(getAverageWinningScore(2021).toPlainString());

//			response = service.request(generateUrl(String.format("/fantasy/v2/league/%s;out=draftresults,standings,settings,scoreboard", thisSeason.getGameLeagueCode())));
//			response = service.request(generateUrl(String.format("/fantasy/v2/league/%s/players;status=A;count=5", thisSeason.getGameLeagueCode())));

			/*List<UserGameTeam> nflSeasons = userGameTeamList.getUserGameTeams().stream().filter(each -> each.getGameCode().equals("nfl")).toList();
			List<LeagueStandings> standings = new ArrayList<>();
			for (UserGameTeam nflSeason : nflSeasons) {
				response = service.request(generateUrl(String.format("/fantasy/v2/league/%s;out=standings", nflSeason.getGameLeagueCode())));
				standings.add(gson.fromJson(response, LeagueStandings.class));
			}
			log.info(standings);*/

            //league settings

            //league scoreboard

            //nfl teams


//			TODO retrieve replacement values from last year
        } catch (Exception e) {
            log.error(e);
        }
    }

    private <T> T executeRequest(String urlContext, Class<T> type) throws YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        return gson.fromJson(service.request(generateUrl(urlContext)), type);
    }

    private BigDecimal getAverageWinningScore(int year) throws YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        UserGameTeamList userGameTeamList = executeRequest(UserGameTeamList.URL, UserGameTeamList.class);

        UserGameTeam season =
                userGameTeamList.getUserGameTeams()
                        .stream()
                        .filter(each -> each.getGameCode().equals("nfl") && each.getGameSeason().equals(Integer.toString(year)))
                        .findAny()
                        .orElseThrow();

        LeagueStandings standings = executeRequest(String.format("/fantasy/v2/league/%s;out=standings", season.getGameLeagueCode()), LeagueStandings.class);

        Matchups yearMatches = null;
        for (YahooTeam team : standings.getStandings().values()) {
            Matchups matchups = executeRequest(
                    String.format("/fantasy/v2/team/%s.t.%s/matchups", season.getGameLeagueCode(), team.getId()),
                    Matchups.class
            );
            yearMatches = (yearMatches == null ? matchups : yearMatches.add(matchups));
            TimeUnit.SECONDS.sleep(5);
        }

        assert yearMatches != null;
        List<BigDecimal> winningScores = yearMatches.getMatchups().stream().map(Matchup::getWinningScore).toList();
        if (!winningScores.isEmpty()) {
            BigDecimal sum = winningScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(new BigDecimal(winningScores.size()), 1, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private String generateUrl(@NonNull String path) {
        if (path.isBlank()) {
            throw new IllegalArgumentException("path is a required argument");
        }
        return String.format("https://fantasysports.yahooapis.com%s?response=json", path);
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
