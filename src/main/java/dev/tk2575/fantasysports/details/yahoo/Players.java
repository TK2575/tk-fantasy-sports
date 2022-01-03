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
						Player.PlayerBuilder player = Player.builder();
						JsonElement playerEntryValue = playerEntry.getValue();
						if (playerEntryValue.isJsonObject()) {
							JsonArray playerArray = playerEntryValue.getAsJsonObject().get("player").getAsJsonArray();
							for (JsonElement playerContent : playerArray) {
								for (JsonElement playerElement : playerContent.getAsJsonArray()) {
									if (playerElement.isJsonObject()) {
										JsonObject each = playerElement.getAsJsonObject();
										if (each.get("player_key") != null) {
											player = player.key(each.get("player_key").getAsString());
										}
										else if (each.get("player_id") != null) {
											player = player.id(Long.parseLong(each.get("player_id").getAsString()));
										}
										else if (each.get("name") != null) {
											JsonObject name = each.get("name").getAsJsonObject();
											player = player.fullName(name.get("full").getAsString()).lastName(name.get("last").getAsString()).firstName(name.get("first").getAsString());
										}
										else if (each.get("editorial_player_key") != null) {
											player = player.editorialPlayerKey(each.get("editorial_player_key").getAsString());
										}
										else if (each.get("display_position") != null) {
											player = player.position(each.get("display_position").getAsString());
										}
									}
								}

							}
							results.add(player.build());
						}
					}
				}
			}

			return new Players(results);
		};
	}
}
