package dev.tk2575.fantasysports.details.sleeper;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.tk2575.fantasysports.core.nfl.PlayerProjection;
import dev.tk2575.fantasysports.core.nfl.Player;
import dev.tk2575.fantasysports.core.nfl.PlayerStats;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerProjectionService {
    
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    
    private final SleeperApiManager api = SleeperApiManager.getInstance();
    
    public List<PlayerProjection> getPreseasonCanonicalProjections(int season) throws SleeperApiManager.SleeperApiServiceException {
        Map<String, SleeperPlayerProjection> positionProjections = getAllPositionProjections(season);
        Map<String, BigDecimal> auctionValues = getAuctionValues(season);
        List<PlayerProjection> results = new ArrayList<>();

        for (Map.Entry<String, SleeperPlayerProjection> projectionEntry : positionProjections.entrySet()) {
            String playerId = projectionEntry.getKey();
            BigDecimal auctionValue = auctionValues.getOrDefault(playerId, BigDecimal.ZERO);
            SleeperPlayerProjection projection = projectionEntry.getValue();
            Position position = projection.getPlayer().getPosition();
            
            var player = Player.builder()
                    .id(playerId)
                    .firstName(projection.getPlayer().getFirstName())
                    .lastName(projection.getPlayer().getLastName())
                    .build();
            
            var stats = buildStats(projection.getStats());
            
            var result = PlayerProjection.builder()
                    .position(position == null ? null : position.toValue())
                    .positions(projection.getPlayer().getFantasyPositions().stream().map(Position::toValue).toList())
                    .player(player)
                    .nflTeam(projection.getPlayer().getTeam())
                    .week(0)
                    .season(season)
                    .points(BigDecimal.valueOf(projection.getStats().getOrDefault("pts_half_ppr", 0.0)))
                    .projectedPrice(auctionValue)
                    .stats(stats)
                    .build();
            results.add(result);
        }
        return results;
    }

    private PlayerStats buildStats(Map<String, Double> stats) {
        return PlayerStats.builder()
                .passAttempts(stats.getOrDefault("pass_att", 0.0).longValue())
                .passCompletions(stats.getOrDefault("pass_cmp", 0.0).longValue())
                .passYards(stats.getOrDefault("pass_yd", 0.0).longValue())
                .passTds(stats.getOrDefault("pass_td", 0.0).longValue())
                .passInts(stats.getOrDefault("pass_int", 0.0).longValue())
                .rushAttempts(stats.getOrDefault("rush_att", 0.0).longValue())
                .rushYards(stats.getOrDefault("rush_yd", 0.0).longValue())
                .rushTds(stats.getOrDefault("rush_td", 0.0).longValue())
                .receptions(stats.getOrDefault("rec", 0.0).longValue())
                .receptionYards(stats.getOrDefault("rec_yd", 0.0).longValue())
                .receptionTds(stats.getOrDefault("rec_td", 0.0).longValue())
                .twoPointConversions(checkMultipleKeys(stats, "pass_2pt", "rec_2pt", "rush_2pt")) //TODO confirm rush_2pt key
                .fumblesLost(stats.getOrDefault("fum_lost", 0.0).longValue())
                .fieldGoalsMade0To19Yards(stats.getOrDefault("fgm_0_19", 0.0).longValue())
                .fieldGoalsMade20To29Yards(stats.getOrDefault("fgm_20_29", 0.0).longValue())
                .fieldGoalsMade30To39Yards(stats.getOrDefault("fgm_30_39", 0.0).longValue())
                .fieldGoalsMade40To49Yards(stats.getOrDefault("fgm_40_49", 0.0).longValue())
                .fieldGoalsMade50PlusYards(stats.getOrDefault("fgm_50p", 0.0).longValue())
                .pointsAfterTouchdown(stats.getOrDefault("xpm", 0.0).longValue())
                .sacks(stats.getOrDefault("sack", 0.0).longValue())
                .safeties(stats.getOrDefault("safety", 0.0).longValue())
                .defensiveInterceptions(stats.getOrDefault("int", 0.0).longValue())
                .fumblesRecovered(stats.getOrDefault("fum_rec", 0.0).longValue())
                .build();
    }

    private long checkMultipleKeys(Map<String, Double> stats, String... keys) {
        long result = 0;
        for (String key : keys) {
            if (stats.containsKey(key)) {
                result = result + stats.get(key).longValue();
            }
        }
        return result;
    }

    Map<String, SleeperPlayerProjection> getAllPositionProjections(int season) throws SleeperApiManager.SleeperApiServiceException {
        Map<String, SleeperPlayerProjection> results = new HashMap<>();
        for (Position position : Position.values()) {
            results.putAll(getProjections(position, season));
        }
        return results;
    }
    
    Map<String, SleeperPlayerProjection> getProjections(Position position, int season) throws SleeperApiManager.SleeperApiServiceException {
        String response = api.request(generateUrl(position, season));
        var token = new TypeToken<ArrayList<SleeperPlayerProjection>>() {}.getType();
        List<SleeperPlayerProjection> projections = gson.fromJson(response, token);
        return projections.stream().collect(Collectors.toMap(SleeperPlayerProjection::getPlayerId, p -> p));
    }

    private String generateUrl(Position position, int season) {
        return String.format("https://api.sleeper.com/projections/nfl/%s?season_type=regular&position[]=%s", season, position);
    }
    
    Map<String, BigDecimal> getAuctionValues(int season) throws SleeperApiManager.SleeperApiServiceException {
        String url = String.format("https://api.sleeper.com/players/nfl/values/regular/%s/half_ppr?idp=false&is_dynasty=false", season);
        var token = new TypeToken<HashMap<String,BigDecimal>>() {}.getType();
        return gson.fromJson(api.request(url), token);
    }

    public static void main(String[] args) throws Exception {
        var service = new PlayerProjectionService();
        Map<String, SleeperPlayerProjection> projections = service.getProjections(Position.RB, 2023);
        projections.values().stream().findFirst().ifPresent(System.out::println);
    }
}
