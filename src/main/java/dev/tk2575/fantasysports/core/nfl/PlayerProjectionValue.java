package dev.tk2575.fantasysports.core.nfl;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
@ToString
public class PlayerProjectionValue extends PlayerProjection {
    
    private final BigDecimal vorp; //value over replacement player (same position)
    private final BigDecimal vorf; //value over replacement flex player
    
    public PlayerProjectionValue(PlayerProjection projection, BigDecimal vorp, BigDecimal vorf) {
        super(projection.getPosition(), projection.getPositions(), projection.getPlayer(), projection.getNflTeam(), projection.getWeek(), projection.getSeason(), projection.getPoints(), projection.getProjectedPrice(), projection.getStats());
        this.vorp = vorp;
        this.vorf = vorf;
    }
    
    
}
