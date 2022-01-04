package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
public class LeagueSettings {
	@SerializedName("draft_type")
	private String draftType;

	@SerializedName("is_auction_draft")
	private int isAuctionDraft;

	@SerializedName("scoring_type")
	private String scoringType;

	@SerializedName("uses_playoff")
	private int usesPlayoff;

	@SerializedName("has_playoff_consolation_games")
	private boolean usesPlayoffConsolationGames;

	@SerializedName("playoff_start_week")
	private int playoffsStartWeek;

	@SerializedName("uses_playoff_reseeding")
	private int usesPlayoffReseeding;

	@SerializedName("uses_lock_eliminated_teams")
	private int usesLockEliminatedTeams;

	@SerializedName("num_playoff_teams")
	private int playoffTeams;

	@SerializedName("num_playoff_consolation_teams")
	private int playoffConsolationTeams;

	@SerializedName("has_multiweek_championship")
	private int hasMultiweekChampionship;

	@SerializedName("waiver_type")
	private String waiverType;

	@SerializedName("waiver_rule")
	private String waiverRule;

	@SerializedName("uses_faab")
	private int usesFaab;

	@SerializedName("draft_time")
	private long draftTime;

	@SerializedName("draft_pick_time")
	private long draftPickTime;

	@SerializedName("trade_end_date")
	private String tradeEndDate;

	@SerializedName("uses_fractional_points")
	private int usesFractionalPoints;

	@SerializedName("uses_negative_points")
	private int usesNegativePoints;

	boolean isUsesNegativePoints() {
		return this.usesNegativePoints == 1;
	}

	boolean isUsesFractionalPoints() {
		return this.usesFractionalPoints == 1;
	}

	boolean isMultiweekChampionship() {
		return this.hasMultiweekChampionship == 1;
	}

	LocalDate getTradeEndDate() {
		return LocalDate.parse(this.tradeEndDate, DateTimeFormatter.ISO_DATE);
	}

	//TODO roster positions

	//TODO stat categories

	//TODO stat modifiers

	boolean isUsesFaab() {
		return this.usesFaab == 1;
	}

	boolean isUsesLockEliminatedTeams() {
		return this.usesLockEliminatedTeams == 1;
	}

	boolean isUsesPlayoffReseeding() {
		return this.usesPlayoffReseeding == 1;
	}

	boolean isUsesPlayoff() {
		return this.usesPlayoff == 1;
	}

	boolean isAuctionDraft() {
		return this.isAuctionDraft == 1;
	}
}
