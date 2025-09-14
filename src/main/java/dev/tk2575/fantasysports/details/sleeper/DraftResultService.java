package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static dev.tk2575.fantasysports.details.DetailsClient.getApplicationProperties;

public class DraftResultService implements SleeperService {

  private final Gson gson = getGson();
  private final SleeperApiManager api = SleeperApiManager.getInstance();

  public List<SleeperDraftPick> getDraftResults(String leagueId) throws SleeperApiManager.SleeperApiServiceException {
    // First API call: Get drafts for the league
    String draftsUrl = String.format("https://api.sleeper.app/v1/league/%s/drafts", leagueId);
    String draftsResponse = api.request(draftsUrl);
    
    Type draftsListType = new TypeToken<List<SleeperDraft>>(){}.getType();
    List<SleeperDraft> drafts = gson.fromJson(draftsResponse, draftsListType);
    
    if (drafts == null || drafts.isEmpty()) {
      throw new SleeperApiManager.SleeperApiServiceException("No drafts found for league: " + leagueId);
    }
    
    // Use the first (and typically only) draft
    SleeperDraft draft = drafts.get(0);
    String draftId = draft.getDraftId();

    // Second API call: Get all picks in the draft
    String picksUrl = String.format("https://api.sleeper.app/v1/draft/%s/picks", draftId);
    String picksResponse = api.request(picksUrl);
    
    Type picksListType = new TypeToken<List<SleeperDraftPick>>(){}.getType();
    return gson.fromJson(picksResponse, picksListType);
  }

  public static void main(String[] args) throws Exception {
    Properties appProps = getApplicationProperties();
    String leagueId = appProps.getProperty("sleeper.league-id");
    DraftResultService service = new DraftResultService();
    List<SleeperDraftPick> draftResults = service.getDraftResults(leagueId);
    
    System.out.println("Draft picks: " + draftResults.size());
    if (!draftResults.isEmpty()) {
      System.out.println("First pick: " + draftResults.get(0));
    }
    
    // Write draft results to JSON file
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
    String fileName = String.format("draft-results-%s.json", timestamp);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      writer.write(service.gson.toJson(draftResults));
      System.out.println("Draft results written to: " + fileName);
    } catch (IOException e) {
      System.err.println("Failed to write draft results to file: " + e.getMessage());
    }
  }
}