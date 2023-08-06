package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.PlayerProjection;
import lombok.AllArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
public class PlayerProjectionWriter {
    
    private final List<PlayerProjection> projections;
    
    public void writeToFile(String filename, String delimiter) throws IOException {
        try (var writer = new PrintWriter(new FileWriter(filename))) {
            List<String> rows = getDelimitedRows(delimiter);
            writer.write(String.join("\n", rows));
        }
        
    }
    
    List<String> getDelimitedRows(CharSequence delimiter) {
        List<String[]> content = new ArrayList<>();
        content.add(headers());
        this.projections.sort(Comparator.comparing(PlayerProjection::getPoints).reversed());
        content.addAll(this.projections.stream().map(this::convertToRow).toList());
        return content.stream().map(row -> String.join(delimiter, row)).toList();
    }
    
    private String[] headers() {
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
                "price"
        };
    }
    
    private String[] convertToRow(PlayerProjection projection) {
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
                projection.getProjectedPrice().toPlainString()
        };
    }
    
}
