package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Getter
public class FantasyPlayerSummary {
  private String player;
  private String position;
  private String team;
  private int weeksStarted;
  private BigDecimal totalPointsWhenStarted;
  private BigDecimal medianPointsWhenStarted;
  
  public static FantasyPlayerSummary from(List<FantasyPlayerWeek> weeks) {
    var first = weeks.get(0);
    var startedWeeks = weeks.stream().filter(FantasyPlayerWeek::isStarted).toList();
    var startedWeeksPoints = startedWeeks.stream().map(FantasyPlayerWeek::getPoints).toList();
    return FantasyPlayerSummary.builder()
            .player(first.getPlayer())
            .position(first.getPosition())
            .team(first.getFantasyTeamName())
            .weeksStarted(startedWeeks.size())
            .medianPointsWhenStarted(startedWeeksPoints)
            .totalPointsWhenStarted(startedWeeksPoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add)).build();
  }
  
  public static List<FantasyPlayerSummary> summarize(List<FantasyPlayerWeek> weeks) {
    return weeks.stream()
        .collect(Collectors.groupingBy(p -> p.getPlayer() + p.getFantasyTeamName()))
        .values().stream()
        .map(FantasyPlayerSummary::from)
        .toList();
  }
}
