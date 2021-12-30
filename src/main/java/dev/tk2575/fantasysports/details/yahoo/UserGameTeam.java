package dev.tk2575.fantasysports.details.yahoo;

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

	long getLeagueId() {
		String s = this.teamKey.substring(this.teamKey.indexOf("l.") + 2);
		return Long.parseLong(s.substring(0, s.indexOf(".t")));
	}

	String getGameLeagueCode() {
		return this.teamKey.substring(0, this.teamKey.indexOf(".t"));
	}
}
