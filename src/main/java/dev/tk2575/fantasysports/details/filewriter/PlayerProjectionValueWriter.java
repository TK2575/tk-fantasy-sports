package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.PlayerProjectionValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerProjectionValueWriter implements FileWriterDetail {
    
    private final List<PlayerProjectionValue> projections;
    
    public PlayerProjectionValueWriter(List<PlayerProjectionValue> projections) {
       List<PlayerProjectionValue> sortedProjections = new ArrayList<>(projections);
       sortedProjections.sort(Comparator.comparing(PlayerProjectionValue::getPoints).reversed());
       this.projections = sortedProjections;
    }
    @Override
    public List<String> getDelimitedRows(CharSequence delimiter) {
        List<String[]> content = new ArrayList<>();
        content.add(getHeaders());
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
                "season",
                "team",
                "week",
                "points",
                "price",
                "ppg",
                "overallRank",
                "positionRank",
                "flexRank",
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
                String.valueOf(projection.getSeason()),
                projection.getNflTeam(),
                String.valueOf(projection.getWeek()),
                projection.getPoints().toString(),
                projection.getProjectedPrice().toPlainString(),
                projection.getPointsPerGame().toPlainString(),
                String.valueOf(projection.getOverallRank()),
                String.valueOf(projection.getPositionRank()),
                String.valueOf(projection.getFlexRank()),
                projection.getVorp().toPlainString(),
                projection.getVorf().toPlainString()
        };
    }
}
