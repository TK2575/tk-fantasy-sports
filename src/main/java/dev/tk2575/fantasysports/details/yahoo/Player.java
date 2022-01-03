package dev.tk2575.fantasysports.details.yahoo;

import lombok.*;

import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class Player {
	private String key;
	private String editorialPlayerKey;
	private Team team;
	private long id;
	private String fullName;
	private String lastName;
	private String firstName;
	private String position;
	private Set<String> eligiblePositions;
}
