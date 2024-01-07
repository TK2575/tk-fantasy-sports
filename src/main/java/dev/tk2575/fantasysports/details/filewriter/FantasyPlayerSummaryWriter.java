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
        .thenComparing(FantasyPlayerSummary::getWeeksStarted).reversed());
    this.stats = sortedStats;
  }


  @Override
  public List<String> getDelimitedRows(CharSequence delimiter) {
    return null;
  }

  @Override
  public String[] getHeaders() {
    return new String[] {
        "player",
        "position",
        "team",
        "weeksStarted",
        "pointsWhenStarted"
    };
  }
}
