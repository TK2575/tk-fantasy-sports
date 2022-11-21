package dev.tk2575.fantasysports.details.yahoo;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    static String playerJson;
    static String playerWithRosterPositionJson;

    static {
        try {
            playerJson = TestUtils.readTestResourceFileToString("Player.json");
            playerWithRosterPositionJson = TestUtils.readTestResourceFileToString("PlayerWithRosterPosition.json");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDeserializationWithoutRosterPosition() {
        assertNotNull(playerJson);

        Player player = YahooUtils.getGson().fromJson(playerJson, Player.class);
        assertNotNull(player);

        assertTrue(player.getKey() != null && !player.getKey().isBlank());
        assertTrue(player.getFullName() != null && !player.getFullName().isBlank());
        assertNull(player.getRosterPosition());
    }

    @Test
    void testDeserializationWithRosterPoition() {
        assertNotNull(playerWithRosterPositionJson);

        Player player = YahooUtils.getGson().fromJson(playerWithRosterPositionJson, Player.class);
        assertNotNull(player);

        assertTrue(player.getKey() != null && !player.getKey().isBlank());
        assertTrue(player.getFullName() != null && !player.getFullName().isBlank());
        assertNotNull(player.getRosterPosition());

    }

}