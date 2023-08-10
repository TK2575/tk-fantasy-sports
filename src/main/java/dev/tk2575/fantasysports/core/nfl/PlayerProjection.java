package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerProjection {
    private String position;
    private List<String> positions;
	private Player player;
    private String nflTeam;
    private int week;
    private int season;
    
    private BigDecimal points;
    private BigDecimal projectedPrice;
    
    private PlayerStats stats;
    
    public BigDecimal getPointsPerGame() {
        //assumes 17 games in regular season
        return this.points.divide(new BigDecimal(17), 2, RoundingMode.HALF_UP);
    }
}
