package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.math.BigDecimal;

@Getter
public class NflPlayerWeek {
	private String position;
	private Player player;
	private Team team;
	private boolean benched;
	private String rosterSlot;
	private String nflTeam;
	private int week;

	private BigDecimal points = BigDecimal.ZERO;
	private BigDecimal percentOwned;

	private long passAttempts = 0L;
	private long passCompletions = 0L;
	private long passYards = 0L;
	private long passTds = 0L;
	private long passInts = 0L;

	private long rushAttempts = 0L;
	private long rushYards = 0L;
	private long rushTds = 0L;

	private long receptionTargets = 0L;
	private long receptions = 0L;
	private long receptionYards = 0L;
	private long receptionTds = 0L;

	private long returnTds = 0L;
	private long twoPointConversions = 0L;
	private long fumblesLost = 0L;

	private long fieldGoalsMade0To19Yards = 0L;
	private long fieldGoalsMade20To29Yards = 0L;
	private long fieldGoalsMade30To39Yards = 0L;
	private long fieldGoalsMade40To49Yards = 0L;
	private long fieldGoalsMade50PlusYards = 0L;
	private long pointsAfterTouchdown = 0L;

	private long sacks = 0L;
	private long safeties = 0L;
	private long defensiveInterceptions = 0L;
	private long fumblesRecovered = 0L;
	private long defensiveTds = 0L;
	private long blockedKicks = 0L;
	private long yardsAllowed = 0L;
}
