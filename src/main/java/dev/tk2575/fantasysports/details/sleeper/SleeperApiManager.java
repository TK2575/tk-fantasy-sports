package dev.tk2575.fantasysports.details.sleeper;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@Log4j2
public class SleeperApiManager {

    private static SleeperApiManager instance;

    static {
        try {
            instance = new SleeperApiManager();
        }
        catch (Exception e) {
            log.fatal("Unable to initialize SleeperApiService");
            log.fatal(e);
            System.exit(1);
        }
    }

    static SleeperApiManager getInstance() { return instance; }

    String request(String urlString) throws SleeperApiServiceException {
        try {
            var request = HttpRequest.newBuilder().uri(new URI(urlString)).GET().timeout(Duration.of(10, SECONDS)).build();
            var client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            }
            log.error(String.format("Received %s code for url: %s", response.statusCode(), urlString));
            throw new SleeperApiServiceException(response.body());
        }
        catch (Exception e) {
            throw new SleeperApiServiceException(e);
        }
    }

    public static class SleeperApiServiceException extends Exception {
        public SleeperApiServiceException(String message) { super (message); }

        public SleeperApiServiceException(Exception e) { super(e); }
    }

    public static void main(String[] args) throws Exception {
        SleeperApiManager api = SleeperApiManager.getInstance();
        String response = api.request("https://api.sleeper.com/projections/nfl/2023?season_type=regular&position[]=K");
        System.out.println(response.substring(0, 100));
    }
}
