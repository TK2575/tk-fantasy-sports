package dev.tk2575.tkfantasysports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeserializationTest {

	static final String rawJson = rawJson();

	final Gson gson = gson();

	private Gson gson() {
		return new GsonBuilder()
				.registerTypeAdapter(UserGameTeamList.class, UserGameTeamList.deserializer())
				.create();
	}

	@Test
	void testDeserialization() {
		assertNotNull(rawJson);
		assertFalse(rawJson.isBlank());

		UserGameTeamList userGameTeam = gson.fromJson(rawJson(), UserGameTeamList.class);
		System.out.println(userGameTeam);
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