package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class LeagueDetails {
	private final String leagueId;
	private final String leagueKey;
	private final String name;
	private final String gameCode;
	private final String season;
	private final String url;
	private final String draftStatus;
	private final int teams;
	private final int currentWeek;
	private final int startWeek;
	private final int endWeek;
	private final LocalDate startDate;
	private final LocalDate endDate;

	private final String draftType;
	private final boolean auctionDraft;
	private final String scoringType;
	private final boolean usesPlayoff;
	private final boolean usesPlayoffConsolationGames;
	private final int playoffsStartWeek;
	private final boolean usesPlayoffReseeding;
	private final boolean usesLockEliminatedTeams;
	private final int playoffTeams;
	private final int playoffConsolationTeams;
	private final boolean multiweekChampionship;
	private final String waiverType;
	private final String waiverRule;
	private final boolean usesFaab;
	private final Instant draftTime;
	private final long draftPickTime;
	private final LocalDate tradeEndDate;
	private final boolean usesFractionalPoints;
	private final boolean usesNegativePoints;
	private final List<RosterPosition> rosterPositions = new ArrayList<>();

	LeagueDetails(@NonNull LeagueInfo info, @NonNull LeagueSettings settings) {
		this.leagueId = info.getLeagueId();
		this.leagueKey = info.getLeagueKey();
		this.name = info.getName();
		this.url = info.getUrl();
		this.draftStatus = info.getDraftStatus();
		this.teams = info.getTeams();
		this.currentWeek = info.getCurrentWeek();
		this.startWeek = info.getStartWeek();
		this.endWeek = info.getEndWeek();
		this.startDate = info.getStartDate();
		this.endDate = info.getEndDate();
		this.gameCode = info.getGameCode();
		this.season = info.getSeason();

		this.draftType = settings.getDraftType();
		this.auctionDraft = settings.isAuctionDraft();
		this.scoringType = settings.getScoringType();
		this.usesPlayoff = settings.isUsesPlayoff();
		this.usesPlayoffConsolationGames = settings.isUsesPlayoffConsolationGames();
		this.playoffsStartWeek = settings.getPlayoffsStartWeek();
		this.usesPlayoffReseeding = settings.isUsesPlayoffReseeding();
		this.usesLockEliminatedTeams = settings.isUsesLockEliminatedTeams();
		this.playoffTeams = settings.getPlayoffTeams();
		this.playoffConsolationTeams = settings.getPlayoffConsolationTeams();
		this.multiweekChampionship = settings.isMultiweekChampionship();
		this.waiverType = settings.getWaiverType();
		this.waiverRule = settings.getWaiverRule();
		this.usesFaab = settings.isUsesFaab();
		this.draftTime = Instant.ofEpochMilli(settings.getDraftTime());
		this.draftPickTime = settings.getDraftPickTime();
		this.tradeEndDate = settings.getTradeEndDate();
		this.usesFractionalPoints = settings.isUsesFractionalPoints();
		this.usesNegativePoints = settings.isUsesNegativePoints();

		this.rosterPositions.addAll(settings.getRosterPositions().getRosterPositions());
	}

	static JsonDeserializer<LeagueDetails> deserializer() {
		return (json, type, jsonDeserializationContext) -> {
			LeagueInfo info = null;
			LeagueSettings settings = null;
			JsonArray leagueResources = json.getAsJsonObject().get("fantasy_content").getAsJsonObject().get("league").getAsJsonArray();
			for (JsonElement eachLeagueResource : leagueResources) {
				JsonObject leagueResourceObject = eachLeagueResource.getAsJsonObject();
				if (leagueResourceObject.get("league_key") != null) {
					info = jsonDeserializationContext.deserialize(eachLeagueResource, LeagueInfo.class);
				}
				else if (leagueResourceObject.get("settings") != null) {
					JsonObject settingsObj = eachLeagueResource.getAsJsonObject().get("settings").getAsJsonArray().get(0).getAsJsonObject();
					settings = jsonDeserializationContext.deserialize(settingsObj, LeagueSettings.class);
				}
			}
			assert info != null;
			assert settings != null;
			return new LeagueDetails(info, settings);
		};
	}
}
