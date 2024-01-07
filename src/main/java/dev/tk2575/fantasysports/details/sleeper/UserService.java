package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Properties;

import static dev.tk2575.fantasysports.details.DetailsClient.getApplicationProperties;

public class UserService implements SleeperService {
  private final Gson gson = getGson();
  private final SleeperApiManager api = SleeperApiManager.getInstance();
  
  List<SleeperUser> getUsers(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
    String url = String.format("https://api.sleeper.app/v1/league/%s/users", leagueId);
    String json = api.request(url);
    return gson.fromJson(json, new TypeToken<List<SleeperUser>>(){}.getType());
  }

  public static void main(String[] args) throws Exception {
    String leagueId = getApplicationProperties().getProperty("sleeper.league-id");
    new UserService().getUsers(leagueId).forEach(System.out::println);
  }
}
