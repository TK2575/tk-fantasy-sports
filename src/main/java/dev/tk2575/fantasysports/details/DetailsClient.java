package dev.tk2575.fantasysports.details;

import dev.tk2575.fantasysports.core.nfl.FantasyPlayerSummary;
import dev.tk2575.fantasysports.core.nfl.FantasyPlayerWeek;
import dev.tk2575.fantasysports.core.nfl.ProjectionCalculationResult;
import dev.tk2575.fantasysports.core.nfl.ProjectionValueCalculator;
import dev.tk2575.fantasysports.details.filewriter.FantasyPlayerSummaryWriter;
import dev.tk2575.fantasysports.details.filewriter.FantasyPlayerWeekWriter;
import dev.tk2575.fantasysports.details.filewriter.PlayerProjectionValueWriter;
import dev.tk2575.fantasysports.details.filewriter.PositionPointValueWriter;
import dev.tk2575.fantasysports.details.sleeper.LeagueService;
import dev.tk2575.fantasysports.details.sleeper.LeagueSettings;
import dev.tk2575.fantasysports.details.sleeper.PlayerProjectionService;
import dev.tk2575.fantasysports.details.sleeper.SleeperApiManager;
import dev.tk2575.fantasysports.details.sleeper.SleeperClient;

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
        
        generateDraftPrepArtifacts(leagueId);
    }
    
    private static void generatePerformanceArtifacts(String leagueId) throws Exception {
        //TODO all weeks
        List<FantasyPlayerWeek> weeklyPlayerStats = SleeperClient.getMatchups(leagueId, 1);
        new FantasyPlayerWeekWriter(weeklyPlayerStats)
                .writeToFile(String.format("performance-%s.tsv", LocalDate.now()), "\t");

        new FantasyPlayerSummaryWriter(FantasyPlayerSummary.summarize(weeklyPlayerStats))
                .writeToFile(String.format("summary-%s.tsv", LocalDate.now()), "\t");
    }

    private static void generateDraftPrepArtifacts(String leagueId) 
        throws SleeperApiManager.SleeperApiServiceException, IOException {
        var projections = new PlayerProjectionService().getPreseasonCanonicalProjections(2023);
        LeagueSettings leagueSettings = new LeagueService().getLeagueSettings(leagueId);

        ProjectionCalculationResult calculation = 
                new ProjectionValueCalculator(projections)
                        .calculate(leagueSettings.getTotalRosters(), leagueSettings.getRosterPositions());
        
        new PlayerProjectionValueWriter(calculation.getPlayers())
                .writeToFile(String.format("projections-%s.tsv", LocalDate.now()), "\t");
        
        new PositionPointValueWriter(calculation.getPositions())
                .writeToFile(String.format("positions-%s.tsv", LocalDate.now()), "\t");
    }

}
