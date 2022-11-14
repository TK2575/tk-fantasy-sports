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
public class DraftResults implements YahooFantasyObject {
	static final String URL = "/fantasy/v2/league/%s/draftresults";

	private final List<DraftResult> results;

	static JsonDeserializer<DraftResults> deserializer() {
		return (json, type, jsonDeserializationContext) -> {
			List<DraftResult> results = new ArrayList<>();

			JsonArray leagueResources = json.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("league").getAsJsonArray();
			for (JsonElement eachLeagueResource : leagueResources) {
				if (eachLeagueResource.isJsonObject() && eachLeagueResource.getAsJsonObject().get("draft_results") != null) {
					JsonObject draftResults = eachLeagueResource.getAsJsonObject().get("draft_results").getAsJsonObject();
					for (Map.Entry<String, JsonElement> draftEntry : draftResults.entrySet()) {
						if (draftEntry.getValue().isJsonObject() && draftEntry.getValue().getAsJsonObject().get("draft_result").isJsonObject()) {
							JsonObject draftResult = draftEntry.getValue().getAsJsonObject().get("draft_result").getAsJsonObject();
							if (draftResult != null) {
								DraftResult result = jsonDeserializationContext.deserialize(draftResult, DraftResult.class);
								results.add(result);
							}
						}
					}
				}
			}
			return new DraftResults(results);
		};
	}
}
