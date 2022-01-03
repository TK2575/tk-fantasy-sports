package dev.tk2575.fantasysports.details.yahoo;

import lombok.*;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class Players {
	private final List<Player> players;
}
