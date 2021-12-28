package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public class YahooUtils {

	private static final Gson gson = gson();

	public static Gson getGson() {
		return gson;
	}

	private static Gson gson() {
		return new GsonBuilder()
				.registerTypeAdapter(UserGameTeamList.class, UserGameTeamList.deserializer())
				.create();
	}
}
