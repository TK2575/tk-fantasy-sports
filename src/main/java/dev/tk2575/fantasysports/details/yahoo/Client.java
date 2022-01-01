package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

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

			UserGameTeam thisSeason =
					userGameTeamList.getUserGameTeams()
							.stream()
							.filter(each -> each.getGameCode().equals("nfl") && each.getGameSeason().equals("2021"))
							.findAny()
							.orElseThrow();

			response = service.request(generateUrl(String.format("/fantasy/v2/league/%s;out=standings,settings,scoreboard", thisSeason.getGameLeagueCode())));
			YahooTeam yahooTeam = gson.fromJson(response, LeagueStandings.class).getStandings().values().stream().findAny().orElseThrow();
			log.info(service.request(generateUrl(String.format("/fantasy/v2/team/%s/draftresults;out=players", yahooTeam.getKey()))));
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
