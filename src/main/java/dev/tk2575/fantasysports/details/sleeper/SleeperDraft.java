package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
public class SleeperDraft {
  private long created;
  private List<String> creators;
  private String draftId;
  private Map<String, Integer> draftOrder;
  private String lastMessageId;
  private long lastMessageTime;
  private long lastPicked;
  private String leagueId;
  private Map<String, Object> metadata;
  private String season;
  private String seasonType;
  private Map<String, Object> settings;
  private String sport;
  private long startTime;
  private String status;
  private String type;
}