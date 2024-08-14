package dev.tk2575.fantasysports.details.sleeper;

import dev.tk2575.fantasysports.core.nfl.FantasyPlayerWeek;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Used to join sleeper data together from varying services in order to form core objects
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SleeperClient {

  public static List<FantasyPlayerWeek> getMatchups(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
    Map<String, String> playersAndPosById = new PlayerService().getPlayersAndPosById();
    Map<Long, String> teamNamesByRosterId = getTeamNamesByRosterId(leagueId);
    List<FantasyPlayerWeek> matchups = new ArrayList<>();
    var svc = new MatchupService();
    for (int week = 1; week <= 17; week++) {
      matchups.addAll(svc.getMatchups(leagueId, week, teamNamesByRosterId, playersAndPosById));
    }
    return matchups;
  }

  private static Map<Long, String> getTeamNamesByRosterId(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
    List<SleeperUser> users = new UserService().getUsers(leagueId);
    List<SleeperRoster> rosters = new RosterService().getRosters(leagueId);

    var ownersByRoster = rosters.stream().collect(Collectors.toMap(SleeperRoster::getRosterId, SleeperRoster::getOwnerId));
    var teamNamesByOwner = users.stream().collect(Collectors.toMap(SleeperUser::getUserId, SleeperUser::getTeamName));
    var displayNamesByOwner = users.stream().collect(Collectors.toMap(SleeperUser::getUserId, SleeperUser::getDisplayName));
    
    return ownersByRoster.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
      String ownerId = e.getValue();
      Optional<String> teamName = teamNamesByOwner.get(ownerId);
      return teamName.orElse(displayNamesByOwner.get(ownerId));
    }));
  }
}
