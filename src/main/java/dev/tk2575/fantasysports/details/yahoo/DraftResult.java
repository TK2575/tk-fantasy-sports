package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
public class DraftResult {
	@SerializedName("team_key")
	private String teamKey;

	@SerializedName("player_key")
	private String playerKey;

	private long pick;
	private long round;
	private long cost;
}
