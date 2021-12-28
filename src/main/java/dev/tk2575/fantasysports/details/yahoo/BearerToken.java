package dev.tk2575.fantasysports.details.yahoo;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Log4j2
public class BearerToken {

	public static OAuth2AccessToken readTokenFromFile() {
		try (FileReader reader = new FileReader(getAccessTokenFile())) {
			return new Gson().fromJson(reader, OAuth2AccessToken.class);
		}
		catch (IOException e) {
			return null;
		}
	}

	private static File getAccessTokenFile() throws FileNotFoundException {
		return ResourceUtils.getFile("temp/access-token.json");
	}

	public static void generate() throws IOException, ExecutionException, InterruptedException {
		File file = getAccessTokenFile();
		if (file.exists()) {
			file.delete();
		}

		YahooAppInfo yahooAppInfo = YahooAppInfo.readAppInfoFromFile();

		final Scanner in = new Scanner(System.in);
		final OAuth20Service service = new ServiceBuilder(yahooAppInfo.getKey())
				.apiSecret(yahooAppInfo.getSecret())
				.callback(OAuthConstants.OOB)
				.build(YahooApi20.instance());

		log.info("Fetching the Request Token...");
		log.info("Got the Request Token!");

		log.info("Now go and authorize ScribeJava here:");
		log.info(service.getAuthorizationUrl());
		log.info("And paste the verifier here");
		log.info(">>");
		final String oauthVerifier = in.nextLine();

		log.info("Trading the Authorization Code for an Access Token...");
		OAuth2AccessToken accessToken = service.getAccessToken(oauthVerifier);
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(new Gson().toJson(accessToken));
		}
		log.info("Got the Access Token!");
		log.info("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
	}

	public static void main(String[] args) throws Exception {
		generate();
	}
}
