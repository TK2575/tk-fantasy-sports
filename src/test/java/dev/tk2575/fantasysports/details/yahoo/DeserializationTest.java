package dev.tk2575.fantasysports.details.yahoo;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeserializationTest {

	static final String rawJson = rawJson();

	@Test
	void testUserGameTeams() {
		UserGameTeamList userGameTeam = YahooUtils.getGson().fromJson(rawJson, UserGameTeamList.class);

		assertNotNull(userGameTeam);
		assertNotNull(userGameTeam.getUserGameTeams());
		assertEquals(11, userGameTeam.getUserGameTeams().size());
	}

	static String rawJson() {
		StringBuilder sb = new StringBuilder();
		try {
			File file = ResourceUtils.getFile("src/test/resources/FantasyContent.json");
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}