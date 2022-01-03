package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class Players {

	static final String URL = "/fantasy/v2/league/%s/players";

	private final List<Player> players;

	static JsonDeserializer<Players> deserializer() {
		return (json, type, jsonDeserializationContext) -> {
			List<Player> results = new ArrayList<>();
			JsonArray leagueResources = json.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("league").getAsJsonArray();
			for (JsonElement eachLeagueResource : leagueResources) {
				if (eachLeagueResource.isJsonObject() && eachLeagueResource.getAsJsonObject().get("players") != null) {
					JsonObject players = eachLeagueResource.getAsJsonObject().get("players").getAsJsonObject();
					for (Map.Entry<String, JsonElement> playerEntry : players.entrySet()) {
						JsonArray playerArray = playerEntry.getValue().getAsJsonObject().get("player").getAsJsonArray();
						if (playerArray != null) {
							System.out.println(playerArray);
						}

					}
				}
			}

			return new Players(results);
		};
	}
}
