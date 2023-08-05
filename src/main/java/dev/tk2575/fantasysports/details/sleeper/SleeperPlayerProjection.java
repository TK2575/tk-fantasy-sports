package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
class SleeperPlayerProjection {
    private Object week;
    private String team;
    private Map<String, Double> stats;
    private String sport;
    private String seasonType;
    private String season;
    private String playerId;
    private SleeperPlayer player;
    private Object opponent;
    private long lastModified;
    private String gameID;
    private Object date;
    private String company;
    private String category;
}
