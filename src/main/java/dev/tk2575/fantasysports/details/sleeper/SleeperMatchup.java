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
class SleeperMatchup {
    private Map<String,BigDecimal> playersPoints;
    private List<BigDecimal> startersPoints;
    private List<String> starters;
    private Long matchupId;
    private BigDecimal customPoints;
    private Long rosterId;
    private List<String> players;
    private BigDecimal points;
}
