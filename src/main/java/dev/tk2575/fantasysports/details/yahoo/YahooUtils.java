package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.NONE)
public class YahooUtils {

	private static final Gson gson = gson();

	public static Gson getGson() {
		return gson;
	}

	private static Gson gson() {
		return new GsonBuilder()
				.registerTypeAdapter(UserGameTeamList.class, UserGameTeamList.deserializer())
				.registerTypeAdapter(LeagueStandings.class, LeagueStandings.deserializer())
				.registerTypeAdapter(DraftResults.class, DraftResults.deserializer())
				.registerTypeAdapter(Players.class, Players.deserializer())
				.registerTypeAdapter(LeagueDetails.class, LeagueDetails.deserializer())
				.registerTypeAdapter(RosterPositions.class, RosterPositions.deserializer())
				.create();
	}
	
	public static BigDecimal roundTwoDecimalPlaces(BigDecimal value) {
		return round(value,2);
	}

	private static BigDecimal round(BigDecimal value, int decimalPlaces) {
		return value.setScale(decimalPlaces, RoundingMode.HALF_UP);
	}
}
