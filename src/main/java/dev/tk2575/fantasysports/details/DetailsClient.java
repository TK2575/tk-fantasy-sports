package dev.tk2575.fantasysports.details;

import dev.tk2575.fantasysports.core.nfl.PlayerProjectionValue;
import dev.tk2575.fantasysports.core.nfl.ProjectionValueCalculator;
import dev.tk2575.fantasysports.details.filewriter.PlayerProjectionValueWriter;
import dev.tk2575.fantasysports.details.filewriter.PlayerProjectionWriter;
import dev.tk2575.fantasysports.details.sleeper.LeagueService;
import dev.tk2575.fantasysports.details.sleeper.LeagueSettings;
import dev.tk2575.fantasysports.details.sleeper.PlayerProjectionService;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class DetailsClient {
    
    private static Properties getApplicationProperties() {
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
        
        var projections = new PlayerProjectionService().getPreseasonCanonicalProjections(2023);
        LeagueSettings leagueSettings = new LeagueService().getLeagueSettings(appProps.getProperty("sleeper.league-id"));
        
/*        List<PlayerProjectionValue> projectionValues = */
        System.out.println(new ProjectionValueCalculator(projections)
                        .calculate(leagueSettings.getTotalRosters(), leagueSettings.getRosterPositions()));
        
        /*new PlayerProjectionWriter(projections)
                .writeToFile(String.format("projections-%s.tsv", LocalDate.now()), "\t");*/
    }
    
}
