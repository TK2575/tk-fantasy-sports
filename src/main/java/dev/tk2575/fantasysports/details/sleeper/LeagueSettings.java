package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;


@ToString
@EqualsAndHashCode
@Getter
public class LeagueSettings {
    private int totalRosters;
    private String status;
    private String sport;
    private long shard;
    private Map<String, Long> settings;
    private String seasonType;
    private String season;
    private Map<String, Double> scoringSettings;
    private List<String> rosterPositions;
    private String previousLeagueID;
    private String name;
    private Object loserBracketID;
    private String leagueID;
    private String lastTransactionID;
    private String lastReadID;
    private String lastPinnedMessageID;
    private long lastMessageTime;
    private String lastMessageTextMap;
    private String lastMessageID;
    private String lastMessageAttachment;
    private boolean lastAuthorIsBot;
    private String lastAuthorID;
    private String lastAuthorDisplayName;
    private String lastAuthorAvatar;
    private String groupID;
    private String draftID;
    private String displayOrder;
    private Object companyID;
    private Object bracketID;
    private String avatar;
}