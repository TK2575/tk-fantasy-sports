package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LeagueDetailsTest {

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

		LeagueDetails leagueDetails = YahooUtils.getGson().fromJson(rawJson, LeagueDetails.class);

		assertNotNull(leagueDetails);
		assertTrue(leagueDetails.getLeagueKey() != null && !leagueDetails.getLeagueKey().isBlank());
		assertTrue(leagueDetails.isAuctionDraft());
		assertTrue(leagueDetails.isUsesPlayoffConsolationGames());
		assertFalse(leagueDetails.isMultiweekChampionship());
		assertTrue(leagueDetails.isUsesFaab());
		assertTrue(leagueDetails.isUsesNegativePoints());

		assertTrue(leagueDetails.getRosterPositions() != null && !leagueDetails.getRosterPositions().isEmpty());
	}

}