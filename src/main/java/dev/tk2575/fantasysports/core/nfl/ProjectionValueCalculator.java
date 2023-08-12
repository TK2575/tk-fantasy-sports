package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Log4j2
public class ProjectionValueCalculator {
    
    private final List<PlayerProjection> projections;
    
    public ProjectionCalculationResult calculate(int teams, List<String> positions) {
        Map<String,Long> rosterSlotCountByPosition = positions.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        final List<PlayerRank> playerRanks = computePlayerRankings(rosterSlotCountByPosition);
        List<PositionPointValue> positionValues = computePosPointValuesByIndex(playerRanks, teams, rosterSlotCountByPosition);
        List<PlayerProjectionValue> playerValues = computePlayerValues(playerRanks, positionValues, rosterSlotCountByPosition);
        return new ProjectionCalculationResult(playerValues, positionValues);
    }

    private List<PlayerProjectionValue> computePlayerValues(List<PlayerRank> playerRanks, List<PositionPointValue> positionValues, Map<String, Long> rosterSlotCountByPosition) {
        Map<String, BigDecimal> replacementValues = 
                positionValues.stream()
                        .collect(Collectors.toMap(PositionPointValue::getPosition, PositionPointValue::getReplacement));
        
        return playerRanks.stream().map(p -> {
            String positionKey = p.getPosition() + (rosterSlotCountByPosition.get(p.getPosition()) > 1 ? "1" : ""); //PositionPointValues may have enumerated positions
            BigDecimal vorp = p.getPointsPerGame().subtract(replacementValues.get(positionKey));
            BigDecimal vorf = p.isFlexEligible() ? p.getPointsPerGame().subtract(replacementValues.get("FLEX1")) : vorp;
            return new PlayerProjectionValue(p, vorp, vorf);
        }).toList();
    }

    private List<PositionPointValue> computePosPointValuesByIndex(List<PlayerRank> rankings, int teams, Map<String, Long> rosterSlotsByPosition) {
        int flexRosterSpots = rosterSlotsByPosition.get("FLEX").intValue();
        List<PlayerRank> replacementFlexPlayers =
                rankings.stream()
                        .filter(PlayerRank::isFlexEligible)
                        .filter(r -> !isStartingAtPosition(r.getPositionRank(), teams, rosterSlotsByPosition.get(r.getPosition())))
                        .toList();
        
        Map<String,List<BigDecimal>> bestValuesByPosition = 
                getBestValuesByPosition(rosterSlotsByPosition, rankings, flexRosterSpots, replacementFlexPlayers);
        Map<String,List<BigDecimal>> replacementValuesByPosition = 
                getReplacementValuesByPosition(rankings, teams, flexRosterSpots, replacementFlexPlayers, rosterSlotsByPosition);

        List<PositionPointValue> results = new ArrayList<>();
        for (String position : rosterSlotsByPosition.keySet()) {
            if (position.equals("BN")) continue;
            
            assert(bestValuesByPosition.containsKey(position));
            List<BigDecimal> bestPoints = bestValuesByPosition.get(position);
            
            assert(replacementValuesByPosition.containsKey(position));
            List<BigDecimal> replacementPoints = replacementValuesByPosition.get(position);
            
            assert(bestPoints.size() == replacementPoints.size());
            for (int i = 0; i < bestPoints.size(); i++) {
                PositionPointValue ppv = 
                        new PositionPointValue(
                                position + (rosterSlotsByPosition.get(position) > 1 ? i+1 : ""), 
                                bestPoints.get(i), 
                                replacementPoints.get(i)
                        );
                results.add(ppv);
            }
        }
        return results;
    }

    private Map<String, List<BigDecimal>> getReplacementValuesByPosition(List<PlayerRank> rankings, int teams, int flexRosterSpots, List<PlayerRank> replacementPlayersArg, Map<String, Long> rosterSlotsByPosition) {
        Map<String, List<BigDecimal>> replacementValuesByPosition = getFlexEligibleReplacementValues(teams, flexRosterSpots, replacementPlayersArg, rosterSlotsByPosition);
        
        for (Map.Entry<String, Long> positionCount : rosterSlotsByPosition.entrySet()) {
            final String position = positionCount.getKey();
            if (List.of("BN","FLEX").contains(position) || PlayerProjectionInterface.getFlexPositions().contains(position)) {
                continue;
            }

            int positionRosterSlots = positionCount.getValue().intValue();
            for (int i = 1; i <= positionRosterSlots; i++) {
                int eye = i;
                var best = rankings.stream()
                        .filter(r -> r.getPosition().equals(position) && 
                                r.getPositionRank() == (teams * positionRosterSlots) + eye)
                        .findFirst().orElseThrow().getPointsPerGame();
                replacementValuesByPosition.merge(position, List.of(best), (a,b) -> {
                    List<BigDecimal> merged = new ArrayList<>(a);
                    merged.addAll(b);
                    return merged;
                });
            }
        }
        
        return replacementValuesByPosition;
    }
    
    private Map<String, List<BigDecimal>> getFlexEligibleReplacementValues(int teams, int flexRosterSpots, List<PlayerRank> replacementPlayersArg, Map<String, Long> rosterSlotsByPositionArg) {
        Map<String, List<BigDecimal>> replacementValuesByPosition = new HashMap<>();
        int flexStarterCount = flexRosterSpots * teams;
        List<PlayerRank> replacementPlayers = new ArrayList<>(replacementPlayersArg);
        replacementPlayers.subList(0, flexStarterCount).clear();

        Map<String, Long> rosterSlotsByPosition = new HashMap<>();
        for (Map.Entry<String, Long> posEntry : rosterSlotsByPositionArg.entrySet()) {
            String pos = posEntry.getKey();
            if (PlayerProjectionInterface.getFlexPositions().contains(pos) || pos.equals("FLEX")) {
                rosterSlotsByPosition.put(pos, posEntry.getValue());
            }
        }

        int i = 0;
        while (!rosterSlotsByPosition.isEmpty()) {
            PlayerRank player = replacementPlayers.get(i);
            var points = player.getPointsPerGame();
            String position = player.getPosition();
            if (!rosterSlotsByPosition.containsKey(position)) {
                position = "FLEX";
            }
            if (rosterSlotsByPosition.containsKey(position)) {
                var positionRosterSlots = rosterSlotsByPosition.get(position);
                if (positionRosterSlots <= 1) {
                    rosterSlotsByPosition.remove(position);
                }
                else {
                    rosterSlotsByPosition.put(position, positionRosterSlots-1);
                }
                replacementValuesByPosition.merge(position, List.of(points), (a,b) -> {
                    List<BigDecimal> merged  = new ArrayList<>(a);
                    merged.addAll(b);
                    return merged;
                });
            }
            i++;
        }
        return replacementValuesByPosition;
    }

    private Map<String, List<BigDecimal>> getBestValuesByPosition(Map<String,Long> rosterSlotsByPosition, 
                                                                  List<PlayerRank> rankings, int flexRosterSpots, 
                                                                  List<PlayerRank> replacementFlexPlayers) {
        Map<String, List<BigDecimal>> bestValuesByPosition = new HashMap<>();
        for (Map.Entry<String, Long> positionCount : rosterSlotsByPosition.entrySet()) {
            final String position = positionCount.getKey();
            if (List.of("BN","FLEX").contains(position)) continue;

            int positionRosterSlots = positionCount.getValue().intValue();
            for (int i = 1; i <= positionRosterSlots; i++) {
                int eye = i;
                var best = rankings.stream()
                        .filter(r -> r.getPosition().equals(position) && r.getPositionRank() == eye)
                        .findFirst().orElseThrow().getPointsPerGame();
                bestValuesByPosition.merge(position, List.of(best), (a,b) -> {
                    List<BigDecimal> merged = new ArrayList<>(a);
                    merged.addAll(b);
                    return merged;
                });
            }
        }

        List<BigDecimal> bestFlexPPG = 
                replacementFlexPlayers.subList(0, flexRosterSpots).stream().map(PlayerRank::getPointsPerGame).toList();
        bestValuesByPosition.put("FLEX", bestFlexPPG);

        return bestValuesByPosition;
    }

    private boolean isStartingAtPosition(int positionRank, int teams, Long slots) {
        return positionRank <= teams * slots;
    }
    
    private List<PlayerRank> computePlayerRankings(Map<String,Long> rosterSlotsByPosition) {
        Map<String,Integer> positionRank = new HashMap<>();
        //set ranking cursor for all positions
        rosterSlotsByPosition.keySet().stream().filter(p -> !List.of("FLEX","BN").contains(p)).forEach(p -> positionRank.put(p, 0));

        List<PlayerProjection> sortedPlayers = 
                projections.stream().sorted(Comparator.comparing(PlayerProjection::getPointsPerGame).reversed()).toList();
        
        List<PlayerRank> rankings = new ArrayList<>();
        int overallRank = 0;
        int flexRank = 0;
        int posRank;
        
        for (PlayerProjection player : sortedPlayers) {
            String position = player.getPosition();
            if (!positionRank.containsKey(position)) continue;
            
            posRank = positionRank.get(position) + 1;
            positionRank.put(position, posRank);
            overallRank++;
            int thisFlexRank = 0;
            
            if (player.isFlexEligible()) {
                flexRank++;
                thisFlexRank = flexRank;
            }
            rankings.add(new PlayerRank(player, overallRank, posRank, thisFlexRank));
        }
        return rankings;
    }


}
