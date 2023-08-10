package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;

public class LeagueService implements SleeperService {
    
    private final Gson gson = getGson();
    
    private final SleeperApiManager api = SleeperApiManager.getInstance();
    
    public LeagueSettings getLeagueSettings(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
        String url = String.format("https://api.sleeper.app/v1/league/%s", leagueId);
        return gson.fromJson(api.request(url), LeagueSettings.class);
    }

    public static void main(String[] args) throws Exception {
        LeagueSettings leagueSettings = new LeagueService().getLeagueSettings("926160489745387520");
        System.out.println(leagueSettings.getRosterPositions());
    }
}
