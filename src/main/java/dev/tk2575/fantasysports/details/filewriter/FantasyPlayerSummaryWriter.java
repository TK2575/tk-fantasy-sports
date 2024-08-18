package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.FantasyPlayerSummary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FantasyPlayerSummaryWriter implements FileWriterDetail {

  private final List<FantasyPlayerSummary> stats;

  public FantasyPlayerSummaryWriter(List<FantasyPlayerSummary> stats) {
    List<FantasyPlayerSummary> sortedStats = new ArrayList<>(stats);
    sortedStats.sort(Comparator.comparing(FantasyPlayerSummary::getPlayer)
        .thenComparing(FantasyPlayerSummary::getWeeksStarted, Comparator.reverseOrder())
        .thenComparing(FantasyPlayerSummary::getTotalPointsWhenStarted, Comparator.reverseOrder()));
    this.stats = sortedStats;
  }


  @Override
  public List<String> getDelimitedRows(CharSequence delimiter) {
    List<String[]> content = new ArrayList<>();
    content.add(getHeaders());
    content.addAll(this.stats.stream().map(this::convertToRow).toList());
    return content.stream().map(row -> String.join(delimiter, row)).toList();
  }

  private String[] convertToRow(FantasyPlayerSummary stat) {
    return new String[]{
        stat.getPlayer(),
        stat.getPosition(),
        stat.getTeam(),
        String.valueOf(stat.getWeeksStarted()),
        stat.getMedianPointsWhenStarted().toString(),
        stat.getTotalPointsWhenStarted().toString()
    };
  }

  @Override
  public String[] getHeaders() {
    return new String[]{
        "player",
        "position",
        "team",
        "weeks_started",
        "median_started_points",
        "total_started_points"
    };
  }
}
