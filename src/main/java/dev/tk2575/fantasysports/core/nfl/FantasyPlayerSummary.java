package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import dev.tk2575.fantasysports.Utils;

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

    FantasyPlayerSummaryBuilder builder = FantasyPlayerSummary.builder()
        .player(first.getPlayer())
        .position(first.getPosition())
        .team(first.getFantasyTeamName())
        .weeksStarted(0)
        .totalPointsWhenStarted(BigDecimal.ZERO)
        .medianPointsWhenStarted(BigDecimal.ZERO);


    var startedWeeksPoints =
        weeks.stream()
            .filter(FantasyPlayerWeek::isStarted)
            .map(FantasyPlayerWeek::getPoints)
            .sorted().toList();

    if (!startedWeeksPoints.isEmpty()) {
      int size = startedWeeksPoints.size();
      BigDecimal midpoint = startedWeeksPoints.get(size / 2);
      BigDecimal medianPointsWhenStarted = midpoint;

      if (size % 2 == 0) {
        medianPointsWhenStarted = midpoint
            .add(startedWeeksPoints.get(size / 2 - 1))
            .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
      }

      builder.weeksStarted(size)
          .medianPointsWhenStarted(medianPointsWhenStarted)
          .totalPointsWhenStarted(startedWeeksPoints.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    return builder.build();
  }

  public static List<FantasyPlayerSummary> summarize(List<FantasyPlayerWeek> weeks) {
    return weeks.stream()
        .collect(Collectors.groupingBy(p -> p.getPlayer() + p.getFantasyTeamName()))
        .values().stream()
        .map(FantasyPlayerSummary::from)
        .toList();
  }
}
