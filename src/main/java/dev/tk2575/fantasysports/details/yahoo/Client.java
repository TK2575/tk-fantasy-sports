package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Client implements Runnable {

	@EventListener(ApplicationReadyEvent.class)
	@Override
	public void run() {
		try {
			YahooFantasyService service = YahooFantasyService.getInstance();
			String response = service.request(generateUrl(UserGameTeamList.URL));
			Gson gson = YahooUtils.getGson();
			UserGameTeamList userGameTeamList = gson.fromJson(response, UserGameTeamList.class);

			UserGameTeam lastSeason =
					userGameTeamList.getUserGameTeams()
							.stream()
							.filter(each -> each.getGameCode().equals("nfl") && each.getGameSeason().equals("2021"))
							.findAny()
							.orElseThrow();

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

//			TODO retrieve average winning score last year
			response = service.request(generateUrl(String.format("/fantasy/v2/league/%s;out=standings", lastSeason.getGameLeagueCode())));
			LeagueStandings lastYearStandings = gson.fromJson(response, LeagueStandings.class);
			Matchups yearMatches = null;
			for (YahooTeam team : lastYearStandings.getStandings().values()) {
				response = service.request(generateUrl(
						String.format("/fantasy/v2/team/%s.t.%s/matchups",
								lastSeason.getGameLeagueCode(),
								team.getId())
				));
				Matchups matchups = gson.fromJson(response, Matchups.class);
				if (yearMatches == null) {
					yearMatches = matchups;
				}
				else {
					yearMatches = yearMatches.add(matchups);
				}
				TimeUnit.SECONDS.sleep(5);
			}
			assert yearMatches != null;
			List<BigDecimal> winningScores = yearMatches.getMatchups().stream().map(Matchup::getWinningScore).toList();
			if (!winningScores.isEmpty()) {
				BigDecimal sum = winningScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
				BigDecimal average = sum.divide(new BigDecimal(winningScores.size()), 1, RoundingMode.HALF_UP);
				log.info(String.format("2021's average winning score was = %s", average.toPlainString())); // 131.7
			}

//			TODO retrieve replacement values from last year
		}
		catch (Exception e) {
			log.error(e);
		}
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
