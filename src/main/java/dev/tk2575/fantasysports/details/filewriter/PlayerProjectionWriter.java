package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.PlayerProjection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerProjectionWriter implements FileWriterDetail {
    
    private final List<PlayerProjection> projections;

    public PlayerProjectionWriter(List<PlayerProjection> projections) {
       List<PlayerProjection> sortedProjections = new ArrayList<>(projections);
       sortedProjections.sort(Comparator.comparing(PlayerProjection::getPoints).reversed());
       this.projections = sortedProjections;
    } 

    public List<String> getDelimitedRows(CharSequence delimiter) {
        List<String[]> content = new ArrayList<>();
        content.add(getHeaders());
        content.addAll(this.projections.stream().map(this::convertToRow).toList());
        return content.stream().map(row -> String.join(delimiter, row)).toList();
    }
    
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
                "ppg"
        };
    }
    
    private String[] convertToRow(PlayerProjection projection) {
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
                projection.getPointsPerGame().toPlainString()
        };
    }
    
}
