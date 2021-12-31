package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.util.*;

@Getter
@ToString
@AllArgsConstructor
public class LeagueStandings {
	static final String URL = "/fantasy/v2/leagues;league_keys=%s;out=standings";

	private SortedMap<Integer,YahooTeam> standings;

	static JsonDeserializer<LeagueStandings> deserializer() {
		return (jsonElement, type, jsonDeserializationContext) -> {
			SortedMap<Integer, YahooTeam> results = new TreeMap<>();
			YahooTeam.YahooTeamBuilder builder = YahooTeam.builder();

			JsonArray leagueResources = jsonElement.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("league").getAsJsonArray();
			for (JsonElement eachLeagueResource : leagueResources) {
				JsonElement standings = eachLeagueResource.getAsJsonObject().get("standings");
				if (standings != null) {
					Set<Map.Entry<String, JsonElement>> teams = standings.getAsJsonArray().get(0).getAsJsonObject().get("teams").getAsJsonObject().entrySet();
					for (Map.Entry<String, JsonElement> teamElements : teams) {
						JsonArray team = teamElements.getValue().getAsJsonObject().get("team").getAsJsonArray();
						for (JsonElement teamElement : team) {
							if (teamElement.isJsonArray()) {

								for (JsonElement field : teamElement.getAsJsonArray()) {
									if (field.isJsonObject()) {
										JsonObject fieldObj = field.getAsJsonObject();
										if (fieldObj.get("team_id") != null) {
											builder = builder.id(fieldObj.get("team_id").getAsInt());
										}
										else if (fieldObj.get("name") != null) {
											builder = builder.name(fieldObj.get("name").getAsString());
										}
										else if (fieldObj.get("faab_balance") != null) {
											builder = builder.faabBalance(fieldObj.get("faab_balance").getAsLong());
										}
										else if (fieldObj.get("number_of_trades") != null) {
											builder = builder.trades(fieldObj.get("number_of_trades").getAsInt());
										}
										else if (fieldObj.get("clinched_playoffs") != null) {
											builder = builder.playoffsClinched(fieldObj.get("clinched_playoffs").getAsInt() == 1);
										}
										else if (fieldObj.get("auction_budget_total") != null) {
											builder = builder.auctionBudget(Long.parseLong(fieldObj.get("auction_budget_total").getAsString()));
										}
										else if (fieldObj.get("managers") != null) {
											builder = parseManagers(builder, fieldObj.get("managers"));
										}

									}
								}
							}
							else if (teamElement.getAsJsonObject().get("team_points") != null) {

							}
							else if (teamElement.getAsJsonObject().get("team_standings") != null) {

							}
						}
					}
				}
			}

			return new LeagueStandings(results);
		};
	}

	private static YahooTeam.YahooTeamBuilder parseManagers(YahooTeam.YahooTeamBuilder builder, JsonElement managers) {
		boolean hasManager = false;
		YahooManager manager;
		for (JsonElement managerElement : managers.getAsJsonArray()) {
			JsonObject managerJson = managerElement.getAsJsonObject().get("manager").getAsJsonObject();
			String nickname = managerJson.get("nickname").getAsString();
			String guid = managerJson.get("guid").getAsString();
			int id = Integer.parseInt(managerJson.get("manager_id").getAsString());

			manager = new YahooManager(id, nickname, guid);
			if (!hasManager) {
				builder.manager(manager);
				hasManager = true;
			}
			else {
				builder.coManager(Optional.of(manager));
			}
		}
		return builder;
	}
}
