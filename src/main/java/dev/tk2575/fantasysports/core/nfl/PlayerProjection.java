package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerProjection {
    private String position;
    private List<String> positions;
	private Player player;
    private String nflTeam;
    private int week;
    private int season;
    
    private BigDecimal points = BigDecimal.ZERO;
    private BigDecimal projectedPrice = BigDecimal.ZERO;
    
    private PlayerStats stats;
}
