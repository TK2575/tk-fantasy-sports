package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
public class SleeperRoster {
  private Long rosterId;
  private String ownerId;
  private List<String> coOwners;
  private Map<String, BigDecimal> settings;
}
