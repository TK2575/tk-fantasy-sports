package dev.tk2575.fantasysports.details;

import dev.tk2575.fantasysports.core.nfl.FantasyPlayerSummary;
import dev.tk2575.fantasysports.core.nfl.FantasyPlayerWeek;
import dev.tk2575.fantasysports.core.nfl.PlayerProjection;
import dev.tk2575.fantasysports.core.nfl.ProjectionCalculationResult;
import dev.tk2575.fantasysports.core.nfl.ProjectionValueCalculator;
import dev.tk2575.fantasysports.details.filereader.AthleticProjectionReader;
import dev.tk2575.fantasysports.details.filewriter.FantasyPlayerSummaryWriter;
import dev.tk2575.fantasysports.details.filewriter.FantasyPlayerWeekWriter;
import dev.tk2575.fantasysports.details.filewriter.PlayerProjectionValueWriter;
import dev.tk2575.fantasysports.details.filewriter.PositionPointValueWriter;
import dev.tk2575.fantasysports.details.sleeper.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class DetailsClient {

  //TODO move to config manager class
  public static Properties getApplicationProperties() {
    Properties appProps = new Properties();

    try (InputStream inputStream =
             Thread.currentThread()
                 .getContextClassLoader()
                 .getResourceAsStream("application.properties")) {
      appProps.load(inputStream);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load application.properties", e);
    }
    return appProps;
  }

  public static void main(String[] args) throws Exception {
    Properties appProps = getApplicationProperties();
    String leagueId = appProps.getProperty("sleeper.league-id");

//    generatePerformanceArtifacts(leagueId);
    generateDraftPrepArtifacts(leagueId);
//		generateDraftResultArtifacts(leagueId);
  }

	private static void generateDraftResultArtifacts(String leagueId)
			throws SleeperApiManager.SleeperApiServiceException {
		DraftResultService svc = new DraftResultService();
		List<SleeperDraftPick> draftResults = svc.getDraftResults(leagueId);
		// TODO (maybe) join with player info
		// TODO create a draft results writer
	}

	//TODO create a weekly team performance artifacts method
  // determines the number of points scored per team (i.e. starting players)
  // computes replacement team points weekly, best team points weekly, target team vorp, dollar per vorp target

  private static void generatePerformanceArtifacts(String leagueId)
      throws SleeperApiManager.SleeperApiServiceException, IOException {
    List<FantasyPlayerWeek> weeklyPlayerStats = SleeperClient.getMatchups(leagueId);

    new FantasyPlayerWeekWriter(weeklyPlayerStats)
        .writeToFile(String.format("performance-%s.tsv", LocalDate.now()), "\t");

    new FantasyPlayerSummaryWriter(FantasyPlayerSummary.summarize(weeklyPlayerStats))
        .writeToFile(String.format("summary-%s.tsv", LocalDate.now()), "\t");
  }

  private static void generateDraftPrepArtifacts(String leagueId)
      throws SleeperApiManager.SleeperApiServiceException, IOException {
    LocalDate today = LocalDate.now();
    var year = today.getYear();
    List<PlayerProjection> projections = new PlayerProjectionService().getPreseasonCanonicalProjections(year);
    LeagueSettings leagueSettings = new LeagueService().getLeagueSettings(leagueId);

    AthleticProjectionReader athletic = new AthleticProjectionReader(year);
    if (athletic.projectionsPresent()) {
      // expects league settings to be updated manually on these projections
      //TODO consider merging or enriching with Sleeper projections for multi-position eligibility, capturing all players
      projections = athletic.readProjections();
    }

    ProjectionCalculationResult calculation =
        new ProjectionValueCalculator(projections)
            .calculate(leagueSettings.getTotalRosters(), leagueSettings.getRosterPositions());


    new PlayerProjectionValueWriter(calculation.getPlayers())
        .writeToFile(String.format("projections-%s.tsv", today), "\t");

    new PositionPointValueWriter(calculation.getPositions())
        .writeToFile(String.format("positions-%s.tsv", today), "\t");
  }


}
