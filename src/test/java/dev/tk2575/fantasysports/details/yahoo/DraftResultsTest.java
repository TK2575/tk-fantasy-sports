package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DraftResultsTest {

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

		DraftResults draftResults = YahooUtils.getGson().fromJson(rawJson, DraftResults.class);
		assertNotNull(draftResults);

		List<DraftResult> results = draftResults.getResults();
		assertTrue(results != null && !results.isEmpty());
		for (DraftResult each : results) {
			assertTrue(each.getTeamKey() != null && !each.getTeamKey().isBlank());
			assertTrue(each.getPlayerKey() != null && !each.getPlayerKey().isBlank());
		}
	}
}