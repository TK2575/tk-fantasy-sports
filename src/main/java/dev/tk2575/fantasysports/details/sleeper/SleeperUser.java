package dev.tk2575.fantasysports.details.sleeper;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Optional;

@ToString
@Getter
@EqualsAndHashCode
public class SleeperUser {
  private String userId;
  private Map<String,String> metadata;
  private String leagueId;
  private String displayName;
  
  Optional<String> getTeamName() {
    return Optional.ofNullable(metadata.get("team_name"));
  }
}
