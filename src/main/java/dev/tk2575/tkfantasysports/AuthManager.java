package dev.tk2575.tkfantasysports;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import lombok.*;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class AuthManager {

	private static final String PROTECTED_RESOURCE_URL
			= "https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games/teams";

	public static void main(String[] args) throws Exception {
		final Scanner in = new Scanner(System.in);

		AppInfo appInfo = readAppInfoFromFile();
		if (appInfo == null) {
			appInfo = readAppInfoFromConsole(in);
		}

		final OAuth20Service service = new ServiceBuilder(appInfo.getKey())
				.apiSecret(appInfo.getSecret())
				.callback(OAuthConstants.OOB)
				.build(YahooApi20.instance());

		System.out.println("=== Yahoo's OAuth Workflow ===");
		System.out.println();

		OAuth2AccessToken accessToken = readTokenFromFile();
		if (accessToken == null) {
			accessToken = generateAccessTokenFromConsole(in, service);
		}
		else {
			accessToken = service.refreshAccessToken(accessToken.getRefreshToken());
		}

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		try (Response response = service.execute(request)) {
			System.out.println("Got it! Lets see what we found...");
			System.out.println();
			System.out.println(response.getCode());
			System.out.println(response.getBody());
		}
		System.out.println();
	}

	private static OAuth2AccessToken generateAccessTokenFromConsole(Scanner in, OAuth20Service service) throws IOException, InterruptedException, ExecutionException {
		OAuth2AccessToken accessToken;
		System.out.println("Fetching the Request Token...");
		System.out.println("Got the Request Token!");
		System.out.println();

		System.out.println("Now go and authorize ScribeJava here:");
		System.out.println(service.getAuthorizationUrl());
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		final String oauthVerifier = in.nextLine();
		System.out.println();

		System.out.println("Trading the Authorization Code for an Access Token...");
		accessToken = service.getAccessToken(oauthVerifier);
		writeTokenToFile(accessToken);
		System.out.println("Got the Access Token!");
		System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
		System.out.println();
		return accessToken;
	}

	private static AppInfo readAppInfoFromConsole(Scanner in) throws IOException {
		AppInfo appInfo;
		System.out.println("Paste the clientId here");
		System.out.print(">>");
		final String key = in.nextLine();
		System.out.println();

		System.out.println("Paste the clientSecret here");
		System.out.print(">>");
		final String secret = in.nextLine();
		System.out.println();

		appInfo = new AppInfo(key, secret);
		writeAppInfoToFile(appInfo);
		return appInfo;
	}

	private static void writeAppInfoToFile(AppInfo appInfo) throws IOException {
		try (FileWriter writer = new FileWriter(getAppInfoFile())) {
			writer.write(new Gson().toJson(appInfo));
		}
	}

	private static AppInfo readAppInfoFromFile() {
		try (FileReader reader = new FileReader(getAppInfoFile())) {
			return new Gson().fromJson(reader, AppInfo.class);
		}
		catch (IOException e) {
			return null;
		}
	}

	private static void writeTokenToFile(OAuth2AccessToken accessToken) throws IOException {
		try (FileWriter writer = new FileWriter(getAccessTokenFile())) {
			writer.write(new Gson().toJson(accessToken));
		}
	}

	private static OAuth2AccessToken readTokenFromFile() {
		try (FileReader reader = new FileReader(getAccessTokenFile())) {
			return new Gson().fromJson(reader, OAuth2AccessToken.class);
		}
		catch (IOException e) {
			return null;
		}
	}

	private static File getAppInfoFile() throws FileNotFoundException {
		return ResourceUtils.getFile("temp/app-info.json");
	}

	private static File getAccessTokenFile() throws FileNotFoundException {
		return ResourceUtils.getFile("temp/access-token.json");
	}

	@RequiredArgsConstructor
	@Getter
	private static class AppInfo {
		private final String key;
		private final String secret;
	}
}
