package dev.tk2575.tkfantasysports;

import com.github.scribejava.core.model.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Log4j2
public class Client implements Runnable {

	@EventListener(ApplicationReadyEvent.class)
	@Override
	public void run() {
		try {
			YahooFantasyService service = YahooFantasyService.getInstance();
			Response response = service.request("https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games/teams");
			if (response.getCode() == 200) {
				log.info(response.getBody());
			}
			else {
				log.error("Bad response: " + response.getCode());
				log.error(response.getMessage());
			}
		}
		catch (IOException | ExecutionException | InterruptedException e) {
			log.error(e);
		}
	}

	public static void main(String[] args) {
		new Client().run();
	}
}