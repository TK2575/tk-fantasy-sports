package dev.tk2575.tkfantasysports;

import com.github.scribejava.apis.YahooApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class YahooFantasyService {

//	FIXME
//	@Value("${app.key}")
	private String key;

//	@Value("${app.secret}")
	private String secret;

	private OAuth2AccessToken accessToken;
	private Instant tokenExpiration;
	private final OAuth20Service service;

	private static YahooFantasyService instance;

	static {
		try {
			instance = new YahooFantasyService();
		}
		catch (Exception e) {
			log.fatal("Unable to initialize YahooFantasyService");
			log.fatal(e);
			System.exit(1);
		}
	}

	private YahooFantasyService() throws IOException, ExecutionException, InterruptedException {
		AppInfo appInfo = AppInfo.readAppInfoFromFile();

		this.service = new ServiceBuilder(appInfo.getKey())
				.apiSecret(appInfo.getSecret())
				.callback(OAuthConstants.OOB)
				.build(YahooApi20.instance());

		this.accessToken = BearerToken.readTokenFromFile();
		if (this.accessToken == null) {
			throw new IllegalArgumentException("Could not find existing bearer token, please generate a new one");
		}
		maybeRefreshToken();
	}

	public static YahooFantasyService getInstance() {
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

	public Response request(String url) throws IOException, ExecutionException, InterruptedException {
		maybeRefreshToken();
		final OAuthRequest request = new OAuthRequest(Verb.GET, url);
		service.signRequest(accessToken, request);
		return service.execute(request);
	}
}
