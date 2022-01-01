package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LeagueStandingsTest {

	static String rawJson;

	static {
		try {
			rawJson = TestUtils.readTestResourceFileToString("LeagueResources.json");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testDeserialization() {
		assertNotNull(rawJson);

		LeagueStandings leagueStandings = YahooUtils.getGson().fromJson(rawJson, LeagueStandings.class);

		assertNotNull(leagueStandings);
		assertNotNull(leagueStandings.getStandings());
		for (YahooTeam team : leagueStandings.getStandings().values()) {
			assertTrue(team.getKey() != null && !team.getKey().isBlank());
			assertNotNull(team.getPoints());
		}
	}

}