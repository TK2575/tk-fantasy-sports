package dev.tk2575.fantasysports.details.yahoo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Matchup {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final String matchupId;
    private final int week;
    private final LocalDate weekStart;
    private final LocalDate weekEnd;
    private final String status;
    private final boolean playoffs;
    private final boolean consolation;
    private final boolean matchupRecapAvailable;
    private final String matchupRecapUrl;
    private final boolean tied;
    @ToString.Include private final String winnerTeamKey;
    @ToString.Include private final List<MatchupTeam> teams;

    public Matchup(MatchupDetails details, List<MatchupTeam> teams) {
        if (details == null || teams == null) {
            throw new IllegalArgumentException("details and teams are required arguments");
        }
        if (teams.size() != 2) {
            throw new IllegalArgumentException("must provide two teams");
        }

        String winnerTeamKey = details.getWinnerTeamKey();
        if (winnerTeamKey == null) {
            throw new IllegalArgumentException("invalid winner team key");
        }
        boolean winningTeamPresent = false;
        for (MatchupTeam team : teams) {
            if (team == null) {
                throw new IllegalArgumentException("team cannot be null");
            }
            if (winnerTeamKey.equals(team.getTeamKey())) {
                winningTeamPresent = true;
            }
        }
        if (!winningTeamPresent) {
            throw new IllegalArgumentException(String.format("winning team not present, expecting %s", winnerTeamKey));
        }

        this.week = Integer.parseInt(details.getWeek());
        this.weekStart = details.getWeekStart();
        this.weekEnd = details.getWeekEnd();
        this.status = details.getStatus();
        this.playoffs = details.getPlayoffs().equals("1");
        this.consolation = details.getConsolation().equals("1");
        this.matchupRecapAvailable = details.getMatchupRecapAvailable().equals("1");
        this.matchupRecapUrl = details.getMatchupRecapUrl();
        this.tied = details.getTied().equals("1");
        this.winnerTeamKey = details.getWinnerTeamKey();
        this.teams = teams;
        this.matchupId = generateMatchupId();
    }

    public String getBoxScoreUrl() {
        return this.matchupRecapUrl.replace("recap", "matchup");
    }

    public BigDecimal getWinningScore() {
        for (MatchupTeam team : teams) {
            if (team.getTeamKey().equals(winnerTeamKey)) {
                return team.getTotalPoints();
            }
        }
        return BigDecimal.ZERO;
    }

    private String generateMatchupId() {
        return String.join("-",
                this.weekEnd.toString(),
                Integer.toString(this.week),
                this.teams.stream()
                        .map(MatchupTeam::getTeamId)
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining("-"))
        );
    }

}
