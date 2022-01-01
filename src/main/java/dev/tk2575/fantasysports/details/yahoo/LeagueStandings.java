package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

import static dev.tk2575.fantasysports.details.yahoo.YahooUtils.roundTwoDecimalPlaces;

@Getter
@ToString
public class LeagueStandings {
	static final String URL = "/fantasy/v2/leagues;league_keys=%s;out=standings";

	private final SortedMap<Integer,YahooTeam> standings = new TreeMap<>();

	public LeagueStandings(List<YahooTeam> teams) {
		List<YahooTeam> sorted = teams.stream().sorted(Comparator.comparing(YahooTeam::getRank)).toList();
		for (YahooTeam each : sorted) {
			this.standings.put(each.getRank(), each);
		}
	}

	static JsonDeserializer<LeagueStandings> deserializer() {
		return (jsonElement, type, jsonDeserializationContext) -> {
			List<YahooTeam> results = new ArrayList<>();
			YahooTeam.YahooTeamBuilder builder = YahooTeam.builder();

			JsonArray leagueResources = jsonElement.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("league").getAsJsonArray();
			for (JsonElement eachLeagueResource : leagueResources) {
				JsonElement standings = eachLeagueResource.getAsJsonObject().get("standings");
				if (standings != null) {
					Set<Map.Entry<String, JsonElement>> teams = standings.getAsJsonArray().get(0).getAsJsonObject().get("teams").getAsJsonObject().entrySet();
					for (Map.Entry<String, JsonElement> teamElements : teams) {
						if (teamElements.getValue().isJsonObject()) {
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
											else if (fieldObj.get("number_of_moves") != null) {
												builder = builder.moves(Long.parseLong(fieldObj.get("number_of_moves").getAsString()));
											}
											else if (fieldObj.get("number_of_trades") != null) {
												builder = builder.trades(fieldObj.get("number_of_trades").getAsLong());
											}
											else if (fieldObj.get("clinched_playoffs") != null) {
												builder = builder.playoffsClinched(fieldObj.get("clinched_playoffs").getAsInt() == 1);
											}
											else if (fieldObj.get("auction_budget_total") != null) {
												builder = builder.auctionBudget(Long.parseLong(fieldObj.get("auction_budget_total").getAsString()));
											}
											else if (fieldObj.get("managers") != null) {
												parseManagers(builder, fieldObj.get("managers"));
											}

										}
									}
								}
								else if (teamElement.isJsonObject() && teamElement.getAsJsonObject().get("team_points") != null) {
									builder.season(teamElement.getAsJsonObject().get("team_points").getAsJsonObject().get("season").getAsString());
								}
								else if (teamElement.isJsonObject() && teamElement.getAsJsonObject().get("team_standings") != null) {
									JsonObject teamStandings = teamElement.getAsJsonObject().get("team_standings").getAsJsonObject();
									builder.rank(Integer.parseInt(teamStandings.get("rank").getAsString()));

									JsonObject outcomeTotals = teamStandings.get("outcome_totals").getAsJsonObject();
									builder.wins(Integer.parseInt(outcomeTotals.get("wins").getAsString())).losses(Integer.parseInt(outcomeTotals.get("losses").getAsString())).ties(outcomeTotals.get("ties").getAsInt());

									builder.points(roundTwoDecimalPlaces(new BigDecimal(teamStandings.get("points_for").getAsString())));
									builder.pointsAgainst(roundTwoDecimalPlaces(new BigDecimal(teamStandings.get("points_against").getAsString())));
								}
							}
						}
						results.add(builder.build());
					}
				}
			}
			return new LeagueStandings(results);
		};
	}

	private static void parseManagers(YahooTeam.YahooTeamBuilder builder, JsonElement managers) {
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
	}
}
