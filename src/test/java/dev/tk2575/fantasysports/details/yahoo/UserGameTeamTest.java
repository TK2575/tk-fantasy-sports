package dev.tk2575.fantasysports.details.yahoo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserGameTeamTest {

	@Test
	void testGetLeagueId() {
		UserGameTeam userGameTeam = UserGameTeam.builder().teamKey("406.l.1193431.t.1").build();
		assertEquals(1193431L, userGameTeam.getLeagueId());
	}

}