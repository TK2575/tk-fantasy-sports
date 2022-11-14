package dev.tk2575.fantasysports.details.yahoo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class LeagueStandingsQueryService extends YahooApiQueryService<LeagueStandings> {

    @Override
    Class<LeagueStandings> clazz() {
        return null;
    }

    @Override
    String URL() {
        return null;
    }

    // TODO move out of query service into some calculation service/client
    BigDecimal getAverageWinningScore(String gameLeagueCode) throws YahooFantasyApiInteractionManager.YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        String url = getApiManager().generateUrl(String.format("/fantasy/v2/league/%s;out=standings", gameLeagueCode));
        LeagueStandings standings = getGson().fromJson(getApiManager().request(url), LeagueStandings.class);

        Matchups yearMatches = null;
        for (YahooTeam team : standings.getStandings().values()) {
            String request = getApiManager()
                    .request(String.format("/fantasy/v2/team/%s.t.%s/matchups", gameLeagueCode, team.getId()));
            Matchups matchups = getGson().fromJson(request, Matchups.class);
            yearMatches = (yearMatches == null ? matchups : yearMatches.add(matchups));
            TimeUnit.SECONDS.sleep(5);
        }

        assert yearMatches != null;
        List<BigDecimal> winningScores = yearMatches.getMatchups().stream().map(Matchup::getWinningScore).toList();
        if (!winningScores.isEmpty()) {
            BigDecimal sum = winningScores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            return sum.divide(new BigDecimal(winningScores.size()), 1, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
