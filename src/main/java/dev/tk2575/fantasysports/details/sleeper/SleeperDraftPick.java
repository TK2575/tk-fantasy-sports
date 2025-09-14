package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
public class SleeperDraftPick {
  private String playerId;
  private String pickedBy;
  private String rosterId;
  private int round;
  private int draftSlot;
  private int pickNo;
  private Map<String, Object> metadata;
  private Object isKeeper;
  private String draftId;
}