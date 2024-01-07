package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Properties;

import static dev.tk2575.fantasysports.details.DetailsClient.getApplicationProperties;

public class RosterService implements SleeperService {
  
  private final Gson gson = getGson();
    
  private final SleeperApiManager api = SleeperApiManager.getInstance();
  
  public List<SleeperRoster> getRosters(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
    String url = String.format("https://api.sleeper.app/v1/league/%s/rosters", leagueId);
    String json = api.request(url);
    return gson.fromJson(json, new TypeToken<List<SleeperRoster>>(){}.getType());
  }

  public static void main(String[] args) throws Exception {
    Properties appProps = getApplicationProperties();
    String leagueId = appProps.getProperty("sleeper.league-id");
    new RosterService().getRosters(leagueId).forEach(System.out::println);
  }
}
