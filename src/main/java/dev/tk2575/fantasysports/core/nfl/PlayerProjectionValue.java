package dev.tk2575.fantasysports.core.nfl;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;


@Getter
@ToString
public class PlayerProjectionValue implements PlayerRankingInterface {
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
    
    private final BigDecimal vorp; //value over replacement player (same position)
    private final BigDecimal vorf; //value over replacement flex player
    
    public PlayerProjectionValue(PlayerRank player, BigDecimal vorp, BigDecimal vorf) {
        this.position = player.getPosition();
        this.positions = player.getPositions();
        this.player = player.getPlayer();
        this.nflTeam = player.getNflTeam();
        this.week = player.getWeek();
        this.season = player.getSeason();
        this.points = player.getPoints();
        this.projectedPrice = player.getProjectedPrice();
        this.stats = player.getStats();
        this.positionRank = player.getPositionRank();
        this.overallRank = player.getOverallRank();
        this.flexRank = player.getFlexRank();
        this.vorp = vorp;
        this.vorf = vorf;
    }
    
    
}
