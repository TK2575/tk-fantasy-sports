package dev.tk2575.fantasysports.details.yahoo;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class Player {
	private String key;
	private String editorialPlayerKey;
	private long id;
	private String fullName;
	private String lastName;
	private String firstName;
	private String position;
}
