package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter(AccessLevel.PACKAGE)
@ToString
public class MatchupDetails {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String week;

    @SerializedName("week_start")
    private String weekStart;

    @SerializedName("week_end")
    private String weekEnd;

    private String status;

    @SerializedName("is_playoffs")
    private String playoffs;

    @SerializedName("is_consolation")
    private String consolation;

    @SerializedName("is_matchup_recap_available")
    private String matchupRecapAvailable;

    @SerializedName("matchup_recap_url")
    private String matchupRecapUrl;

    @SerializedName("matchup_recap_title")
    private String matchupRecapTitle;

    @SerializedName("is_tied")
    private String tied;

    @SerializedName("winner_team_key")
    private String winnerTeamKey;

    public LocalDate getWeekStart() {
        return LocalDate.parse(weekStart, DATE_FORMAT);
    }

    public LocalDate getWeekEnd() {
        return LocalDate.parse(weekEnd, DATE_FORMAT);
    }
}
