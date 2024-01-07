package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerService implements SleeperService {
  private final Gson gson = getGson();
  private final SleeperApiManager api;
  
  public PlayerService() {
    this.api = new SleeperApiManager();
  }
  
  // For testing
  protected PlayerService(SleeperApiManager api) {
    this.api = api;
  }
  
  public Map<String, String> getPlayersAndPosById() throws SleeperApiManager.SleeperApiServiceException {
      return getPlayers().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
        SleeperPlayer player = e.getValue();
        Position position = player.getPosition();
        return String.join("---", player.getFullName(), position == null ? "UNK" : position.toValue());
      }));
  }
  
  Map<String,SleeperPlayer> getPlayers() throws SleeperApiManager.SleeperApiServiceException {
    PlayerCache cache = getFromCache();
    LocalDate today = LocalDate.now();
    if (cache != null && !cache.isStale(today)) {
        return cache.players;
    } 
    Map<String,SleeperPlayer> result = getPlayersFromApi();
    try {
      writeCache(today, result);
    } catch (IOException e) {
      // cache write failed, but we have the data, so just log and move on
      e.printStackTrace();
    }
    return result;
  }

  private PlayerCache getFromCache() {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(PlayerCache.CACHE_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
    } catch (IOException e) {
      // file missing or unreadable, need to write fresh cache
    }
    
    return sb.isEmpty() 
        ? null 
        : this.gson.fromJson(sb.toString(), PlayerCache.class);
  }

  private void writeCache(LocalDate today, Map<String, SleeperPlayer> result) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(PlayerCache.CACHE_FILE))) {
      String date = PlayerCache.DATE_FORMAT.format(today);
      writer.write(this.gson.toJson(new PlayerCache(date, result)));
    }
  }

  private Map<String, SleeperPlayer> getPlayersFromApi() throws SleeperApiManager.SleeperApiServiceException {
    String response = this.api.request("https://api.sleeper.app/v1/players/nfl");
    return this.gson.fromJson(response, (new TypeToken<Map<String, SleeperPlayer>>(){}).getType());
  }
  
  @RequiredArgsConstructor
  static class PlayerCache {
    static final String CACHE_FILE = "temp/player-cache.json";
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
    
    final String lastUpdated;
    final Map<String, SleeperPlayer> players;
    
    boolean isStale(LocalDate now) {
      return LocalDate.parse(this.lastUpdated, DATE_FORMAT).isBefore(now);
    }
  }

  public static void main(String[] args) throws Exception {
    Map<String, SleeperPlayer> players = new PlayerService().getPlayers();
    System.out.println(players.size());

  }
}
