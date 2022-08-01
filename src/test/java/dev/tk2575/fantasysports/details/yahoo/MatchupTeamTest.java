package dev.tk2575.fantasysports.details.yahoo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MatchupTeamTest {

    @Test
    void testTeamIdHappyPath() {
        MatchupTeam matchupTeam = new MatchupTeam("406.l.41236.t.12", BigDecimal.ONE, new BigDecimal("130.00"), new BigDecimal("125.00"));
        assertEquals(12, matchupTeam.getTeamId());
    }

}