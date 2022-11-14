package dev.tk2575.fantasysports.details.yahoo;

import lombok.NonNull;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class UserGameTeamListService {

    private static final String URL = "/fantasy/v2/users;use_login=1/games/teams";

    UserGameTeamList getResults(@NonNull String gameCode, @NonNull Integer year) throws YahooFantasyApiInteractionManager.YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        YahooFantasyApiInteractionManager apiManager = YahooFantasyApiInteractionManager.getInstance();
        var json = apiManager.request(apiManager.generateUrl(URL));
        UserGameTeamList list = YahooUtils.getGson().fromJson(json, UserGameTeamList.class);
        var subset = list.getUserGameTeams().stream()
                .filter(each -> each.getGameCode().equals("nfl") && each.getGameSeason().equals(Integer.toString(2022)))
                .toList();

        return new UserGameTeamList(subset);
    }
}
