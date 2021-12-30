package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserGameTeamListTest {

	static String rawJson;

	static {
		try {
			rawJson = TestUtils.readTestResourceFileToString("UserGamesTeams.json");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testDeserialization() {
		assertNotNull(rawJson);

		UserGameTeamList userGameTeam = YahooUtils.getGson().fromJson(rawJson, UserGameTeamList.class);

		assertNotNull(userGameTeam);
		assertNotNull(userGameTeam.getUserGameTeams());
		assertEquals(11, userGameTeam.getUserGameTeams().size());
	}

}