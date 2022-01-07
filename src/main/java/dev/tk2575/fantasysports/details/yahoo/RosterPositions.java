package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class RosterPositions {
	private final List<RosterPosition> rosterPositions;

	static JsonDeserializer<RosterPositions> deserializer() {
		return (json, type, jsonDeserializationContext) -> {
			List<RosterPosition> results = new ArrayList<>();
			if (json.isJsonArray()) {
				for (JsonElement jsonElement : json.getAsJsonArray()) {
					if (jsonElement.isJsonObject()) {
						results.add(jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("roster_position"), RosterPosition.class));
					}
				}
			}
			return new RosterPositions(results);
		};
	}
}
