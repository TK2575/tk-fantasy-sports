package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerStats {
    private long passAttempts;
	private long passCompletions;
	private long passYards;
	private long passTds;
	private long passInts;

	private long rushAttempts;
	private long rushYards;
	private long rushTds;

	private long receptionTargets;
	private long receptions;
	private long receptionYards;
	private long receptionTds;

	private long returnTds;
	private long twoPointConversions;
	private long fumblesLost;

	private long fieldGoalsMade0To19Yards;
	private long fieldGoalsMade20To29Yards;
	private long fieldGoalsMade30To39Yards;
	private long fieldGoalsMade40To49Yards;
	private long fieldGoalsMade50PlusYards;
	private long pointsAfterTouchdown;

	private long sacks;
	private long safeties;
	private long defensiveInterceptions;
	private long fumblesRecovered;
	private long defensiveTds;
	private long blockedKicks;
	private long yardsAllowed;
}
