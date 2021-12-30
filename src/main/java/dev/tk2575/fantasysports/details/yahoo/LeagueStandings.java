package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonDeserializer;
import lombok.*;

import java.util.SortedMap;

@Getter
@ToString
@AllArgsConstructor
public class LeagueStandings {
	static final String URL = "/fantasy/v2/leagues;league_keys=%s;out=standings";

	private SortedMap<Integer,YahooTeam> standings;

	static JsonDeserializer<UserGameTeamList> deserializer() {
		return (jsonElement, type, jsonDeserializationContext) -> {
			return null;
		};
		//TODO
	}
}
