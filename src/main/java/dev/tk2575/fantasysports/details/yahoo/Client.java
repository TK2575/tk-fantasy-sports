package dev.tk2575.fantasysports.details.yahoo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Log4j2
public class Client implements Runnable {

    private final YahooFantasyApiInteractionManager service = YahooFantasyApiInteractionManager.getInstance();

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void run() {
        try {
            var nfl2022 = new UserGameTeamListService().getResults("nfl", 2022);
            var userGameTeam = nfl2022.getUserGameTeams().get(0);
            DraftResults results = new DraftResultsQueryService(userGameTeam.getGameLeagueCode()).getResults();
            log.info(results);

            /*String urlContext = String.format("/fantasy/v2/team/%s/roster;week=1", userGameTeam.getTeamKey());
            String response = service.request(service.generateUrl(urlContext));
            log.info(response);*/

        } catch (Exception e) {
            log.error(e);
        }
    }

    // LeagueDetails/LeagueInfo/LeagueSettings/LeagueStandings

    // Matchups / Matchup / MatchupDetails / MatchupTeam

    // Players / PlayerWithRosterPosition.json / Team (NFL?)

    // UserGameTeamList / UserGameTeam / YahooTeam

    // Roster Positions / Roster Position

    // YahooAppInfo / YahooFantasyService / YahooFantasyServiceException / YahooUtils / BearerToken

    public static void main(String[] args) {
        new Client().run();
    }
}
