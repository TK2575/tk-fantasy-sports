package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class NflPlayerWeek {
	private String position;
	private Player player;
	private FantasyTeam fantasyTeam;
	private boolean benched;
	private String rosterSlot;
	private String nflTeam;
	private int week;

	private BigDecimal points = BigDecimal.ZERO;
	private BigDecimal percentOwned;

	private PlayerStats stats;
}
