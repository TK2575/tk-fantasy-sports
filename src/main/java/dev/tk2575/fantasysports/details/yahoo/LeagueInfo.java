package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class LeagueInfo {
	@SerializedName("league_id")
	private String leagueId;

	@SerializedName("league_key")
	private String leagueKey;

	private String name;
	private String url;

	@SerializedName("draft_status")
	private String draftStatus;

	@SerializedName("num_teams")
	private int teams;

	@SerializedName("current_week")
	private int currentWeek;

	@SerializedName("start_week")
	private int startWeek;

	@SerializedName("start_date")
	private String startDate;

	@SerializedName("end_week")
	private int endWeek;

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("game_code")
	private String gameCode;

	private String season;

	LocalDate getStartDate() {
		return LocalDate.parse(this.startDate, DateTimeFormatter.ISO_DATE);
	}

	LocalDate getEndDate() {
		return LocalDate.parse(this.endDate, DateTimeFormatter.ISO_DATE);
	}

}
