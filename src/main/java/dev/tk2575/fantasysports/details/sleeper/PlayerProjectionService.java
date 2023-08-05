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

class PlayerProjectionService {
    
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    
    private final SleeperApiManager api = SleeperApiManager.getInstance();
    
    List<PlayerProjection> getPreseasonCanonicalProjections(int season) throws SleeperApiManager.SleeperApiServiceException {
        Map<String, SleeperPlayerProjection> positionProjections = getAllPositionProjections(season);
        Map<String, BigDecimal> auctionValues = getAuctionValues(season);
        List<PlayerProjection> results = new ArrayList<>();

        for (Map.Entry<String, SleeperPlayerProjection> projectionEntry : positionProjections.entrySet()) {
            String playerId = projectionEntry.getKey();
            BigDecimal auctionValue = auctionValues.get(playerId);
            SleeperPlayerProjection projection = projectionEntry.getValue();
            
            var player = Player.builder()
                    .id(playerId)
                    .firstName(projection.getPlayer().getFirstName())
                    .lastName(projection.getPlayer().getLastName())
                    .build();
            
            var stats = buildStats(projection.getStats());
            
            var result = PlayerProjection.builder()
                    .position(projection.getPlayer().getPosition().toValue())
                    .positions(projection.getPlayer().getFantasyPositions().stream().map(Position::toValue).toList())
                    .player(player)
                    .nflTeam(projection.getPlayer().getTeam())
                    .week(0)
                    .season(season)
                    .points(BigDecimal.valueOf(projection.getStats().get("pts_half_ppr")))
                    .projectedPrice(auctionValue)
                    .stats(stats)
                    .build();
            results.add(result);
        }
        return results;
    }

    private PlayerStats buildStats(Map<String, Double> stats) {
        //TODO implement
        return PlayerStats.builder().build();
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
        PlayerProjectionService service = new PlayerProjectionService();
//        Map<String, PlayerProjection> projections = service.getAllPositionProjections(2023);
        Map<String, BigDecimal> values = service.getAuctionValues(2023);
        System.out.println(values.size());
        System.out.println(values.get("1264"));
    }
}
