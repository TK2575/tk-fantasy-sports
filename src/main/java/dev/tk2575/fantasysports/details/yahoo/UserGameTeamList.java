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
@RequiredArgsConstructor
@ToString
public class UserGameTeamList {
	static final String URL = "/fantasy/v2/users;use_login=1/games/teams";

	private final List<UserGameTeam> userGameTeams;

	static JsonDeserializer<UserGameTeamList> deserializer() {
		return (jsonElement, type, jsonDeserializationContext) -> {
			List<UserGameTeam> results = new ArrayList<>();
			UserGameTeam.UserGameTeamBuilder builder = UserGameTeam.builder();

			JsonElement users = jsonElement.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("users");
			for (Map.Entry<String, JsonElement> eachUser : users.getAsJsonObject().entrySet()) {
				if (!eachUser.getKey().equals("count")) {
					JsonArray user = eachUser.getValue().getAsJsonObject().get("user").getAsJsonArray();
					String guid = user.get(0).getAsJsonObject().get("guid").getAsString();
					if (guid != null && !guid.isBlank()) {
						builder = builder.userGuid(guid);

						JsonObject games = user.get(1).getAsJsonObject().get("games").getAsJsonObject();
						for (Map.Entry<String, JsonElement> eachGame : games.entrySet()) {
							JsonElement eachGameValue = eachGame.getValue();
							if (eachGameValue.isJsonObject()) {
								JsonArray game = eachGameValue.getAsJsonObject().get("game").getAsJsonArray();
								builder = parseGameFields(builder, game);

								JsonElement teamsElement = game.get(1).getAsJsonObject().get("teams");
								if (teamsElement.isJsonObject()) {
									JsonObject teams = teamsElement.getAsJsonObject();
									for (Map.Entry<String, JsonElement> eachTeam : teams.entrySet()) {
										if (!eachTeam.getKey().equals("count")) {
											builder = parseTeamFields(builder, eachTeam);
											results.add(builder.build());
										}
									}
								}
							}
						}
					}
				}
			}
			return new UserGameTeamList(results);
		};
	}

	private static UserGameTeam.UserGameTeamBuilder parseGameFields(UserGameTeam.UserGameTeamBuilder builder, JsonArray game) {
		JsonObject gameFields = game.get(0).getAsJsonObject();
		long gameKey = gameFields.get("game_key").getAsLong();
		String gameName = gameFields.get("name").getAsString();
		String gameCode = gameFields.get("code").getAsString();
		String gameSeason = gameFields.get("season").getAsString();
		builder = builder.gameKey(gameKey).gameName(gameName).gameCode(gameCode).gameSeason(gameSeason);
		return builder;
	}

	private static UserGameTeam.UserGameTeamBuilder parseTeamFields(UserGameTeam.UserGameTeamBuilder builder, Map.Entry<String, JsonElement> eachTeam) {
		JsonArray teamFields = eachTeam.getValue().getAsJsonObject().get("team").getAsJsonArray().get(0).getAsJsonArray();
		String teamKey = null;
		String teamUrl = null;
		for (JsonElement teamField : teamFields) {
			if (teamKey == null) {
				JsonElement teamKeyElement = teamField.getAsJsonObject().get("team_key");
				if (teamKeyElement != null) teamKey = teamKeyElement.getAsString();
			}
			if (teamUrl == null) {
				JsonElement teamUrlElement = teamField.getAsJsonObject().get("url");
				if (teamUrlElement != null) teamUrl = teamUrlElement.getAsString();
			}
		}
		builder = builder.teamKey(teamKey).teamUrl(teamUrl);
		return builder;
	}
}
