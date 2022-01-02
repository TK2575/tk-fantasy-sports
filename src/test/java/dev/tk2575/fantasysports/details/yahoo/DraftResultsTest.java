package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DraftResultsTest {

	static String rawJson;

	static {
		try {
			rawJson = TestUtils.readTestResourceFileToString("DraftResults.json");
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
		assertNotNull(draftResults.getResults());
		for (DraftResult each : draftResults.getResults()) {
			assertTrue(each.getTeamKey() != null && !each.getTeamKey().isBlank());
			assertTrue(each.getPlayerKey() != null && !each.getPlayerKey().isBlank());
		}
	}
}