package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class Player {
	private String key;
	private String editorialPlayerKey;
	private long id;
	private String fullName;
	private String lastName;
	private String firstName;
	private String position;
	private String rosterPosition;

	static JsonDeserializer<Player> deserializer() {
		return (json, type, jsonDeserializationContext) -> {
			JsonArray playerArray = json.getAsJsonObject().get("player").getAsJsonArray();
			Player.PlayerBuilder player = Player.builder();
			for (JsonElement playerContent : playerArray) {
				if (playerContent.isJsonArray()) {
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
				else if (playerContent.isJsonObject()) {
					for (Map.Entry<String, JsonElement> each : playerContent.getAsJsonObject().entrySet()) {
						if (each.getKey().equals("selected_position")) {
							for (JsonElement jsonElement : each.getValue().getAsJsonArray()) {
								for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
									if (entry.getKey().equals("position")) {
										player = player.rosterPosition(entry.getValue().getAsString());
									}
								}
							}
						}
					}
				}
			}
			return player.build();
		};
	}
}
