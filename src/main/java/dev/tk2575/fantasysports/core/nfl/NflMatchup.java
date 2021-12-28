package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class NflMatchup {
	private int week;

	private Team team;
	private Team opponent;

	private boolean win;
	private boolean playoffs;

	private BigDecimal points;
	private BigDecimal netVsProjected;
	private BigDecimal benchPoints;
	private BigDecimal optimalPoints;
}
