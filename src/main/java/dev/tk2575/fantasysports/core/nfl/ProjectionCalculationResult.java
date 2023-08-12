package dev.tk2575.fantasysports.core.nfl;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProjectionCalculationResult {
    
    private final List<PlayerProjectionValue> players;
    private final List<PositionPointValue> positions;
    
    public ProjectionCalculationResult(List<PlayerProjectionValue> players, List<PositionPointValue> positions) {
        this.players = players;
        this.positions = positions;
        var best = positions.stream()
                        .map(PositionPointValue::getBest)
                        .reduce(BigDecimal::add)
                        .orElseThrow();
        
        var replacement = positions.stream()
                        .map(PositionPointValue::getReplacement)
                        .reduce(BigDecimal::add)
                        .orElseThrow();
        
        this.positions.add(new PositionPointValue("Total", best, replacement));
    }
}
