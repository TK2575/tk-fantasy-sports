package dev.tk2575.tkfantasysports;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserGameTeam {

	private String userGuid;
	private long gameKey;
	private String gameName;
	private String gameCode;
	private String gameSeason;
	private String teamKey;
	private String teamUrl;
	//TODO managers?
}
