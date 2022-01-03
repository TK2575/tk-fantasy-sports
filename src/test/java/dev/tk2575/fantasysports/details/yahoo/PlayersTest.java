package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayersTest {

	static String rawJson;

	static {
		try {
			rawJson = TestUtils.readTestResourceFileToString("Players.json");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testDeserialization() {
		assertNotNull(rawJson);

		Players players = YahooUtils.getGson().fromJson(rawJson, Players.class);
		assertNotNull(players);

		List<Player> playersList = players.getPlayers();
		assertTrue(playersList != null && !playersList.isEmpty());
		for (Player player : playersList) {
			assertTrue(player.getKey() != null && !player.getKey().isBlank());
			assertTrue(player.getFullName() != null && !player.getFullName().isBlank());
		}

	}

}