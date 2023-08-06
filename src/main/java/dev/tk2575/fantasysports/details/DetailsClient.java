package dev.tk2575.fantasysports.details;

import dev.tk2575.fantasysports.details.filewriter.PlayerProjectionWriter;
import dev.tk2575.fantasysports.details.sleeper.PlayerProjectionService;

import java.time.LocalDate;

public class DetailsClient {
    public static void main(String[] args) throws Exception {
        var projections = new PlayerProjectionService().getPreseasonCanonicalProjections(2023);
        new PlayerProjectionWriter(projections).writeToFile(String.format("projections-%s.tsv", LocalDate.now()), "\t");
    }
}
