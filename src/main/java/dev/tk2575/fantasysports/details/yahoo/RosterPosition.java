package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@ToString
public class RosterPosition {
	private String position;

	@SerializedName("position_type")
	private String positionType;

	private int count;

	@SerializedName("is_starting_position")
	private long startingPosition;

	boolean isStartingPosition() {
		return this.startingPosition == 1;
	}
}
