package dev.tk2575.fantasysports.details.filereader;

import dev.tk2575.fantasysports.core.nfl.PlayerProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
public class AthleticProjectionReader {

  private static final String BASE_PATH = "athletic-projections";

  private final int year;

  public boolean projectionsPresent() {
    try {
      return !readYearFolder().isEmpty();
    } catch (Exception e) {
      log.warn("Could not find athletic projections for {}", year);
      log.warn(e.getMessage());
      log.warn(e.getCause());
      return false;
    }
  }

  // read all TSV files in the year folder and create PlayerProjection objects
  public List<PlayerProjection> readProjections() throws IOException {
    List<PlayerProjection> projections = new ArrayList<>();
    for (String tsvFile : readYearFolder()) {
      projections.addAll(readProjectionsFromFile(tsvFile));
    }
    return projections;
  }

  private Collection<? extends PlayerProjection> readProjectionsFromFile(String fileName) throws IOException {
    List<PlayerProjection> projections = new ArrayList<>();
    Map<String, Integer> headers = null;
    String filePath = getYearPath() + "/" + fileName;
    Resource resource = new ClassPathResource(filePath);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split("\t");
        if (headers == null) {
          headers = new HashMap<>();
          for (int i = 0; i < fields.length; i++) {
            headers.put(fields[i], i);
          }
          continue;
        }
        projections.add(buildPlayerProjection(fields, headers, fileName));
      }
    }
    return projections;
  }

  private PlayerProjection buildPlayerProjection(String[] fields, Map<String, Integer> headers, String fileName) {
    String position = getPrefixBeforePeriod(fileName).toUpperCase();
    return PlayerProjection.builder()
        .position(position)
        .positions(List.of(position))
        .player(new AthleticPlayer(fields[headers.get("Player")]))
        .nflTeam(fields[headers.get("TM")])
        .week(0)
        .season(year)
        .points(new BigDecimal(fields[headers.get("FPS")]))
        .projectedPrice(new BigDecimal(fields[headers.get("AUC$")].replace("$", "")))
        .stats(null) //TODO retrieve for each present in file
        .build();
  }

  private static String getPrefixBeforePeriod(String input) {
    int periodIndex = input.indexOf('.');
    if (periodIndex != -1) {
      return input.substring(0, periodIndex);
    }
    return input; // Return the original string if no period is found
  }

  // find all TSV files in the year folder
  private List<String> readYearFolder() throws IOException {
    List<String> tsvFiles = new ArrayList<>();
    Resource resource = new ClassPathResource(getYearPath());

    if (resource.exists()) {
      File folder = resource.getFile();
      File[] files = folder.listFiles((dir, name) -> name.endsWith(".tsv"));
      if (files != null) {
        for (File file : files) {
          tsvFiles.add(file.getName());
        }
      }
    }
    return tsvFiles;
  }

  private String getYearPath() {
    return BASE_PATH + "/" + year;
  }

  public static void main(String[] args) throws Exception {
    AthleticProjectionReader reader = new AthleticProjectionReader(2024);
    if (reader.projectionsPresent()) {
      List<PlayerProjection> projections = reader.readProjections();
      System.out.println(projections.size());
    }
  }
}
