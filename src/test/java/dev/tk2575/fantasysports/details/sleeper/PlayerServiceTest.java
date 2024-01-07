package dev.tk2575.fantasysports.details.sleeper;

import dev.tk2575.fantasysports.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerServiceTest {
  
  @Test
  void testGetPlayers_deserialization() throws Exception {
    SleeperApiManager api = mock(SleeperApiManager.class);
    String response = TestUtils.readTestResourceFileToString("details/sleeper/PlayerResponse.json");
    when(api.request(anyString())).thenReturn(response);
    assertEquals(2, new PlayerService(api).getPlayers().size());
  }
  
  //TODO add test for cache

}