package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.FantasyPlayerWeek;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FantasyPlayerWeekWriter implements FileWriterDetail {
  
  private final List<FantasyPlayerWeek> stats;
  
  public FantasyPlayerWeekWriter(List<FantasyPlayerWeek> stats) {
    List<FantasyPlayerWeek> sortedStats = new ArrayList<>(stats);
    sortedStats.sort(Comparator.comparing(FantasyPlayerWeek::getWeek)
        .thenComparing(FantasyPlayerWeek::getFantasyTeamName)
        .thenComparing(FantasyPlayerWeek::getPosition)
        .thenComparing(FantasyPlayerWeek::isStarted)
        .thenComparing(FantasyPlayerWeek::getPlayer));
    this.stats = stats;
  }

  @Override
  public List<String> getDelimitedRows(CharSequence delimiter) {
   List<String[]> content = new ArrayList<>();
      content.add(getHeaders());
      content.addAll(this.stats.stream().map(this::convertToRow).toList());
      return content.stream().map(row -> String.join(delimiter, row)).toList();
  }

  private String[] convertToRow(FantasyPlayerWeek stat) {
    return new String[] {
        stat.getPlayer(),
        stat.getFantasyTeamName(),
        String.valueOf(stat.getWeek()),
        stat.getPosition(),
        String.valueOf(stat.isStarted()),
        stat.getPoints().toString()
    };
  }

  @Override
  public String[] getHeaders() {
    return new String[] {
        "player", 
        "team",
        "week",
        "position",
        "started",
        "points"
    };
  }
}
