package dev.tk2575.fantasysports.core.nfl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public interface PlayerProjectionInterface {
    String getPosition();
    List<String> getPositions();
    Player getPlayer();
    String getNflTeam();
    int getWeek();
    int getSeason();
    BigDecimal getPoints();
    BigDecimal getProjectedPrice();
    PlayerStats getStats();
    
    default BigDecimal getPointsPerGame() {
        //assumes 17 games in regular season
        return this.getPoints().divide(new BigDecimal(17), 2, RoundingMode.HALF_UP);
    }
    
    default boolean isFlexEligible() {
        return getPositions().stream().anyMatch(getFlexPositions()::contains);
    }
    
    static Set<String> getFlexPositions() {
        return Set.of("RB", "WR", "TE");
    }
    
    
}
