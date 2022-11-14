package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Client implements Runnable {

    private final YahooFantasyApiInteractionManager service = YahooFantasyApiInteractionManager.getInstance();

    private UserGameTeamList userGameTeamList;

    @EventListener(ApplicationReadyEvent.class)
    @Override
    public void run() {
        try {
            UserGameTeamList nfl2022 = new UserGameTeamListService().getResults("nfl", 2022);
            String gameLeagueCode = nfl2022.getUserGameTeams().get(0).getGameLeagueCode();

        } catch (Exception e) {
            log.error(e);
        }
    }

    private void rosterData(UserGameTeam season) throws YahooFantasyApiInteractionManager.YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        String response = service.request(generateUrl(String.format("/fantasy/v2/team/%s/roster;week=1", season.getTeamKey())));
        log.info(response);
    }

    // LeagueDetails/LeagueInfo/LeagueSettings/LeagueStandings

    // Matchups / Matchup / MatchupDetails / MatchupTeam

    // Players / Player / Team (NFL?)

    // UserGameTeamList / UserGameTeam / YahooTeam

    // Roster Positions / Roster Position

    // YahooAppInfo / YahooFantasyService / YahooFantasyServiceException / YahooUtils / BearerToken

    public static void main(String[] args) {
        new Client().run();
    }
}
