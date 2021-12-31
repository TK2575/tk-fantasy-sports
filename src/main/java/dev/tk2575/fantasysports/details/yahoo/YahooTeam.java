package dev.tk2575.fantasysports.details.yahoo;

import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class YahooTeam {
	private int id;
	private String name;
	private long faabBalance;
	private int trades;
	private boolean playoffsClinched;
	private long auctionBudget;
	private YahooManager manager;
	private Optional<YahooManager> coManager;
	private BigDecimal points;
	private BigDecimal pointsAgainst;
	private int wins;
	private int losses;
}
