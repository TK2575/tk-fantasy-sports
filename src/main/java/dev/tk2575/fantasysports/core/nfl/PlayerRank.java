package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerRank {
    
    private final String playerId;
    private final String position;
    private final BigDecimal points;
    private final BigDecimal pointsPerGame;
    private final BigDecimal price;
    private final int positionRank;
    private final int overallRank; //across all positions
    private final int flexRank; //rank across all flex positions
    private final int flexStartingRank; //not good enough to start in their actual position but good enough to start in flex
    private final int nonStarterRank; //replacement level flex ranking
    
    public PlayerRank(PlayerProjection player, int overallRank, int positionRank, int flexRank, int flexStartingRank, int nonStarterRank) {
        this.playerId = player.getPlayer().getId();
        this.position = player.getPosition();
        this.points = player.getPoints();
        this.pointsPerGame = player.getPointsPerGame();
        this.price = player.getProjectedPrice();
        this.positionRank = positionRank;
        this.overallRank = overallRank;
        this.flexRank = flexRank;
        this.flexStartingRank = flexStartingRank;
        this.nonStarterRank = nonStarterRank;
    }
    
}
