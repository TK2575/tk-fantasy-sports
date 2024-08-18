package dev.tk2575.fantasysports.details.filereader;

import dev.tk2575.fantasysports.core.nfl.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AthleticPlayer implements Player {

  private final String name;

  @Override
  public String getId() {
    return null;
  }

  @Override
  public String getFirstName() {
    return name.split(" ")[0];
  }

  @Override
  public String getLastName() {
    return name.split(" ")[1];
  }

  @Override
  public boolean hasId() {
    return false;
  }
}
