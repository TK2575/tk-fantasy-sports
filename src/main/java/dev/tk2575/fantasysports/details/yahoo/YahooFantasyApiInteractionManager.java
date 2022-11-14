package dev.tk2575.fantasysports.details.yahoo;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
class YahooFantasyApiInteractionManager {

//	FIXME
//	@Value("${app.key}")
	private String key;

//	@Value("${app.secret}")
	private String secret;

	private OAuth2AccessToken accessToken;
	private Instant tokenExpiration;
	private final OAuth20Service service;

	private final Gson gson = YahooUtils.getGson();

	private static YahooFantasyApiInteractionManager instance;

	static {
		try {
			instance = new YahooFantasyApiInteractionManager();
		}
		catch (Exception e) {
			log.fatal("Unable to initialize YahooFantasyService");
			log.fatal(e);
			System.exit(1);
		}
	}

	private YahooFantasyApiInteractionManager() throws IOException, ExecutionException, InterruptedException {
		YahooAppInfo yahooAppInfo = YahooAppInfo.readAppInfoFromFile();

		this.service = new ServiceBuilder(yahooAppInfo.getKey())
				.apiSecret(yahooAppInfo.getSecret())
				.callback(OAuthConstants.OOB)
				.build(YahooApi20.instance());

		this.accessToken = BearerToken.readTokenFromFile();
		if (this.accessToken == null) {
			throw new IllegalArgumentException("Could not find existing bearer token, please generate a new one");
		}
		maybeRefreshToken();
	}

	static YahooFantasyApiInteractionManager getInstance() {
		return instance;
	}

	private void maybeRefreshToken() throws IOException, ExecutionException, InterruptedException {
		Instant now = Instant.now();
		if (this.tokenExpiration == null || !now.isBefore(this.tokenExpiration)) {
			log.info("Refreshing Bearer Token");
			this.accessToken = service.refreshAccessToken(this.accessToken.getRefreshToken());
			this.tokenExpiration = now.plusSeconds(this.accessToken.getExpiresIn());
		}
	}

	String request(String url) throws IOException, ExecutionException, InterruptedException, YahooFantasyServiceException {
		Response response = null;
		try {
			maybeRefreshToken();
			final OAuthRequest request = new OAuthRequest(Verb.GET, url);
			service.signRequest(accessToken, request);
			response = service.execute(request);
		}
		catch (Exception e) {
			throw new YahooFantasyServiceException(e);
		}
		if (response.getCode() == 200) {
			return response.getBody();
		}
		log.error(String.format("Received %s code for url: %s", response.getCode(), url));
		throw new YahooFantasyServiceException(response.getMessage());
	}

	String generateUrl(@NonNull String path) {
		if (path.isBlank()) {
			throw new IllegalArgumentException("path is a required argument");
		}
		return String.format("https://fantasysports.yahooapis.com%s?response=json", path);
	}

	public static class YahooFantasyServiceException extends Exception {
		public YahooFantasyServiceException(String message) {
			super(message);
		}

		public YahooFantasyServiceException(Exception e) {
			super(e);
		}
	}
}
