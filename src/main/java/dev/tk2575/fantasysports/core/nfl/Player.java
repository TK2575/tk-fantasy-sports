package dev.tk2575.fantasysports.core.nfl;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Player {
	private String firstName;
	private String lastName;
	private String id;
	
	public String getName() {
		return firstName + " " + lastName;
	}
}
