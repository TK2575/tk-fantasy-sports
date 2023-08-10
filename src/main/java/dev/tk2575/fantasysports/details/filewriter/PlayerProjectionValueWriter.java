package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.PlayerProjectionValue;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class PlayerProjectionValueWriter implements FileWriterDetail {
    
    private final List<PlayerProjectionValue> projections;
    @Override
    public List<String> getDelimitedRows(CharSequence delimiter) {
        List<String[]> content = new ArrayList<>();
        content.add(getHeaders());
        this.projections.sort(Comparator.comparing(PlayerProjectionValue::getPoints).reversed());
        content.addAll(this.projections.stream().map(this::convertToRow).toList());
        return content.stream().map(row -> String.join(delimiter, row)).toList();
    }
    
    @Override
    public String[] getHeaders() {
        return new String[] {
                "playerId",
                "firstName",
                "lastName",
                "position",
                "positions",
                "season",
                "team",
                "week",
                "points",
                "price",
                "ppg",
                "vorp",
                "vorf"
        };
    }
    
    private String[] convertToRow(PlayerProjectionValue projection) {
        return new String[] {
                projection.getPlayer().getId(),
                projection.getPlayer().getFirstName(),
                projection.getPlayer().getLastName(),
                projection.getPosition(),
                String.join(",", projection.getPositions()),
                String.valueOf(projection.getSeason()),
                projection.getNflTeam(),
                String.valueOf(projection.getWeek()),
                projection.getPoints().toString(),
                projection.getProjectedPrice().toPlainString(),
                projection.getPointsPerGame().toPlainString(),
                projection.getVorp().toPlainString(),
                projection.getVorf().toPlainString()
        };
    }
}
