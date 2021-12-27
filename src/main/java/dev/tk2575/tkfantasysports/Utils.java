package dev.tk2575.tkfantasysports;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	public static Gson getGson() {
		return new GsonBuilder()
				.registerTypeAdapter(UserGameTeamList.class, UserGameTeamList.deserializer())
				.create();
	}
}
