package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Matchups implements YahooFantasyObject {
    static final String URL = "/fantasy/v2/league/%s.t.%s/matchups";

    private final Map<String, Matchup> matchups = new HashMap<>();

    public Matchups(List<Matchup> matchups) {
        matchups.forEach(matchup -> this.matchups.put(matchup.getMatchupId(), matchup));
    }

    public List<Matchup> getMatchups() {
        return this.matchups.values().stream().toList();
    }

    public Matchups add(Matchups moreMatchups) {
        return this.add(moreMatchups.getMatchups());
    }

    public Matchups add(List<Matchup> moreMatchups) {
        List<Matchup> allMatchups = new ArrayList<>(getMatchups());
        allMatchups.addAll(moreMatchups);
        return new Matchups(allMatchups);
    }

    static JsonDeserializer<Matchups> deserializer() {
        return (json, type, jsonDeserializationContext) -> {
            List<Matchup> results = new ArrayList<>();

            JsonArray teamResources = json.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("team").getAsJsonArray();
            for (JsonElement eachTeamResource : teamResources) {
                if (eachTeamResource.isJsonObject() && eachTeamResource.getAsJsonObject().get("matchups") != null) {
                    JsonObject matchupObjects = eachTeamResource.getAsJsonObject().get("matchups").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> matchupObjectEntry : matchupObjects.entrySet()) {
                        if (matchupObjectEntry.getValue().isJsonObject() && matchupObjectEntry.getValue().getAsJsonObject().get("matchup").isJsonObject()) {
                            JsonObject matchup = matchupObjectEntry.getValue().getAsJsonObject().get("matchup").getAsJsonObject();
                            if (matchup != null) {
                                MatchupDetails details = jsonDeserializationContext.deserialize(matchup, MatchupDetails.class);
                                List<MatchupTeam> teams = new ArrayList<>();

                                if (matchup.get("0") != null && matchup.get("0").isJsonObject()) {
                                    JsonObject teamObjects = matchup.get("0").getAsJsonObject().get("teams").getAsJsonObject();
                                    for (Map.Entry<String, JsonElement> teamElement : teamObjects.entrySet()) {
                                        String teamKey = null;
                                        BigDecimal winProbability = null, totalPoints = null, projectedPoints = null;
                                        if (!teamElement.getKey().equals("count")) {
                                            JsonArray teamArray = teamElement.getValue().getAsJsonObject().get("team").getAsJsonArray();
                                            for (JsonElement teamArrayElement : teamArray) {
                                                if (teamArrayElement.isJsonArray()) {
                                                    teamKey = teamArrayElement.getAsJsonArray().get(0).getAsJsonObject().get("team_key").getAsString();
                                                }
                                                else if (teamArrayElement.isJsonObject()) {
                                                    for (Map.Entry<String, JsonElement> pointsObject : teamArrayElement.getAsJsonObject().entrySet()) {
                                                        if (pointsObject.getKey().equals("win_probability")) {
                                                            winProbability = pointsObject.getValue().getAsBigDecimal();
                                                        } else if (pointsObject.getKey().equals("team_points")) {
                                                            totalPoints = pointsObject.getValue().getAsJsonObject().get("total").getAsBigDecimal();
                                                        } else if (pointsObject.getKey().equals("team_projected_points")) {
                                                            projectedPoints = pointsObject.getValue().getAsJsonObject().get("total").getAsBigDecimal();
                                                        }
                                                    }
                                                }
                                            }
                                            teams.add(new MatchupTeam(teamKey, winProbability, totalPoints, projectedPoints));
                                        }
                                    }
                                }
                                results.add(new Matchup(details, teams));

                            }
                        }
                    }
                }
            }
            return new Matchups(results);
        };
    }
}
