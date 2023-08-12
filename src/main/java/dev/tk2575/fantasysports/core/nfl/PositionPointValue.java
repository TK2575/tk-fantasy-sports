package dev.tk2575.fantasysports.core.nfl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@ToString
public class PositionPointValue {
    private final String position;
    private final BigDecimal best;
    private final BigDecimal replacement;
}
