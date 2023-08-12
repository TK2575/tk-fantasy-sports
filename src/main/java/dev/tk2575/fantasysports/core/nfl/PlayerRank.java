package dev.tk2575.fantasysports.core.nfl;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
public class PlayerRank implements PlayerRankingInterface {
    private final String position;
    private final List<String> positions;
	private final Player player;
    private final String nflTeam;
    private final int week;
    private final int season;
    
    private final BigDecimal points;
    private final BigDecimal projectedPrice;
    
    private final PlayerStats stats;
    
    private final int positionRank;
    private final int overallRank; 
    private final int flexRank;
    
    public PlayerRank(PlayerProjection player, int overallRank, int positionRank, int flexRank) {
        this.position = player.getPosition();
        this.positions = player.getPositions();
        this.player = player.getPlayer();
        this.nflTeam = player.getNflTeam();
        this.week = player.getWeek();
        this.season = player.getSeason();
        this.points = player.getPoints();
        this.projectedPrice = player.getProjectedPrice();
        this.stats = player.getStats();
        
        this.positionRank = positionRank;
        this.overallRank = overallRank;
        this.flexRank = flexRank;
    }
    
    
}
