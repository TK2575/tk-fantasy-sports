package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
            if (json.isJsonNull()) return null;
            List<Player> results = new ArrayList<>();
            JsonObject players = json.getAsJsonObject().get("players").getAsJsonObject();
            for (Map.Entry<String, JsonElement> playerEntry : players.entrySet()) {
                JsonElement playerEntryValue = playerEntry.getValue();
                if (playerEntryValue.isJsonObject()) {
                    results.add(jsonDeserializationContext.deserialize(playerEntryValue, Player.class));
                }
            }
            return new Players(results);
        };
    }
}
