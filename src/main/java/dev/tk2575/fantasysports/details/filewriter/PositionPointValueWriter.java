package dev.tk2575.fantasysports.details.filewriter;

import dev.tk2575.fantasysports.core.nfl.PositionPointValue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PositionPointValueWriter implements FileWriterDetail {
    
    private final List<PositionPointValue> positions;
    
    //TODO either lookup these positions specifically or find a generic sorting means
    private final static List<String> sortOrder = List.of(
            "QB",
            "RB1",
            "RB2",
            "WR1",
            "WR2",
            "TE",
            "FLEX1",
            "FLEX2",
            "K",
            "DEF",
            "Total"
    );
    
    public PositionPointValueWriter(List<PositionPointValue> positions) {
        ArrayList<PositionPointValue> sortedPositions = new ArrayList<>(positions);
        sortedPositions.sort(Comparator.comparing(c -> sortOrder.indexOf(c.getPosition())));
        this.positions = sortedPositions;
    }

    @Override
    public List<String> getDelimitedRows(CharSequence delimiter) {
        List<String[]> content = new ArrayList<>();
        content.add(getHeaders());
        content.addAll(this.positions.stream().map(this::convertToRow).toList());
        return content.stream().map(row -> String.join(delimiter, row)).toList();
    }

    @Override
    public String[] getHeaders() {
        return new String[] {
                "position",
                "best",
                "replacement"
        };
    }
    
    private String[] convertToRow(PositionPointValue position) {
        return new String[] {
                position.getPosition(),
                position.getBest().toString(),
                position.getReplacement().toString()
        };
    }
}
