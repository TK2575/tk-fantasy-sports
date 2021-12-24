package dev.tk2575.tkfantasysports;

import com.google.gson.Gson;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Scanner;

@RequiredArgsConstructor
@Getter
@Log4j2
public class AppInfo {
	private final String key;
	private final String secret;

	private static File getAppInfoFile() throws FileNotFoundException {
		return ResourceUtils.getFile("temp/app-info.json");
	}

	public static void writeAppInfoToFile(AppInfo appInfo) throws IOException {
		try (FileWriter writer = new FileWriter(getAppInfoFile())) {
			writer.write(new Gson().toJson(appInfo));
		}
	}

	public static AppInfo readAppInfoFromFile() {
		try (FileReader reader = new FileReader(getAppInfoFile())) {
			return new Gson().fromJson(reader, AppInfo.class);
		}
		catch (IOException e) {
			return null;
		}
	}

	public static void generate() throws IOException {
		File file = getAppInfoFile();
		if (file.exists()) {
			file.delete();
		}

		final Scanner in = new Scanner(System.in);

		log.info("Paste the clientId here");
		log.info(">>");
		final String key = in.nextLine();

		log.info("Paste the clientSecret here");
		log.info(">>");
		final String secret = in.nextLine();

		writeAppInfoToFile(new AppInfo(key, secret));
	}

	public static void main(String[] args) throws Exception {
		generate();
	}
}
