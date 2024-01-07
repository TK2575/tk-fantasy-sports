package dev.tk2575.fantasysports.details.sleeper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SleeperMatchupPlayerPoints {
  private Map<String,BigDecimal> values;
}
