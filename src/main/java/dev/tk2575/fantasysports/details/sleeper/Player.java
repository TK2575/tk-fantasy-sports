package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
class Player {
    private long yearsExp;
    private String team;
    private Position position;
    private Long newsUpdated;
    private Metadata metadata;
    private String lastName;
    private String injuryStatus;
    private Object injuryStartDate;
    private Object injuryNotes;
    private Object injuryBodyPart;
    private String firstName;
    private List<Position> fantasyPositions;
}