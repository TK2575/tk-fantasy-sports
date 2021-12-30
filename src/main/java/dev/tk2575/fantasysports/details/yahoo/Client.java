package dev.tk2575.fantasysports.details.yahoo;

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
			UserGameTeamList userGameTeamList = YahooUtils.getGson().fromJson(response, UserGameTeamList.class);

			UserGameTeam thisSeason =
					userGameTeamList.getUserGameTeams()
							.stream()
							.filter(each -> each.getGameCode().equals("nfl") && each.getGameSeason().equals("2021"))
							.findAny()
							.orElseThrow();

			response = service.request(generateUrl(String.format("/fantasy/v2/leagues;league_keys=%s;out=standings,settings,scoreboard", thisSeason.getGameLeagueCode())));
			log.info(response);
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
