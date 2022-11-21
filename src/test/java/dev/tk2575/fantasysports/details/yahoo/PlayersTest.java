package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayersTest {

	static String playersJson;
	static String playersWithRosterPositionJson;

	static {
		try {
			playersJson = TestUtils.readTestResourceFileToString("Players.json");
			playersWithRosterPositionJson = TestUtils.readTestResourceFileToString("PlayersWithRosterPosition.json");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testDeserializationWithoutRosterPosition() {
		assertNotNull(playersJson);

		Players players = YahooUtils.getGson().fromJson(playersJson, Players.class);
		assertNotNull(players);

		List<Player> playersList = players.getPlayers();
		assertTrue(playersList != null && !playersList.isEmpty());
		for (Player player : playersList) {
			assertTrue(player.getKey() != null && !player.getKey().isBlank());
			assertTrue(player.getFullName() != null && !player.getFullName().isBlank());
			assertNull(player.getRosterPosition());
		}
	}

	@Test
	void testDeserializationWithRosterPosition() {
		assertNotNull(playersWithRosterPositionJson);

		Players playersWithRosterPosition = YahooUtils.getGson().fromJson(playersWithRosterPositionJson, Players.class);
		assertNotNull(playersWithRosterPosition);

		List<Player> players = playersWithRosterPosition.getPlayers();
		assertTrue(players != null && !players.isEmpty());
		for (Player player : players) {
			assertTrue(player.getKey() != null && !player.getKey().isBlank());
			assertTrue(player.getFullName() != null && !player.getFullName().isBlank());
			assertNotNull(player.getRosterPosition());
		}
	}

}