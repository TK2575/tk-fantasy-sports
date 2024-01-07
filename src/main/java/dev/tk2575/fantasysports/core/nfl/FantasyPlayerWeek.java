package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Getter
public class FantasyPlayerWeek {
  private final String player;
  private final String fantasyTeamName;
  private final int week;
  private final String position;
  private final boolean started;
  private final BigDecimal points;
}
