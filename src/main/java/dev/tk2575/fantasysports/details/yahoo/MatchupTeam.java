package dev.tk2575.fantasysports.details.yahoo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class MatchupTeam {
    private String teamKey;
    @ToString.Exclude private BigDecimal winProbability;
    private BigDecimal totalPoints;
    private BigDecimal projectedPoints;

    public int getTeamId() {
        return Integer.parseInt(teamKey.substring(teamKey.lastIndexOf(".t.")+3));
    }
}
