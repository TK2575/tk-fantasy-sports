package dev.tk2575.fantasysports.core.nfl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ProjectionValueCalculator {
    
    private static final Set<String> flexEligiblePositions = Set.of("RB", "WR", "TE");
    
    private final List<PlayerProjection> projections;
    
    public List<PositionPointValue> calculate(int teams, List<String> positions) {
        //calculate position replacement ranks
        Map<String,Long> rosterSlotsByPosition = positions.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        final List<PlayerRank> rankings = computePlayerRankings(teams, rosterSlotsByPosition);

        List<PositionPointValue> positionPointValues = computePositionPointValue(rankings, teams, rosterSlotsByPosition);
        positionPointValues.addAll(computeFlexPointValue(rankings, rosterSlotsByPosition.get("FLEX")));

        //identify point values at each ranking
        
        //compute value over replacement for each player
        return positionPointValues;
    }
    
    //TODO refactor all below for complexity and shared code, ideally non-looping and non-branching
    private List<PlayerRank> computePlayerRankings(int teams, Map<String,Long> rosterSlotsByPosition) {
        Map<String,Integer> positionRank = new HashMap<>();
        //set ranking cursor for all positions
        rosterSlotsByPosition.keySet().stream().filter(p -> !List.of("FLEX","BN").contains(p)).forEach(p -> positionRank.put(p, 0));

        List<PlayerProjection> sortedPlayers = 
                projections.stream().sorted(Comparator.comparing(PlayerProjection::getPointsPerGame).reversed()).toList();
        
        List<PlayerRank> rankings = new ArrayList<>();
        int overallRank = 0;
        int flexRank = 0;
        int flexStarterRank = 0;
        int replacementRank = 0;
        int posRank;
        
        for (PlayerProjection player : sortedPlayers) {
            String position = player.getPosition();
            if (!positionRank.containsKey(position)) continue;
            
            posRank = positionRank.get(position) + 1;
            positionRank.put(position, posRank);
            overallRank++;
            int thisFlexRank = 0;
            int thisFlexStarterRank = 0;
            int thisNonStarterRank = 0;
            
            if (flexEligiblePositions.contains(position)) {
                flexRank++;
                thisFlexRank = flexRank;
                if (!isPositionStarter(posRank, teams, rosterSlotsByPosition.get(position))) {
                    flexStarterRank++;
                    thisFlexStarterRank = flexStarterRank;
                    if (!isFlexStarter(overallRank, teams, rosterSlotsByPosition)) {
                        replacementRank++;
                        thisNonStarterRank = replacementRank;
                    }
                }
            }
            rankings.add(new PlayerRank(player, overallRank, posRank, thisFlexRank, thisFlexStarterRank, thisNonStarterRank));
        }
        return rankings;
    }

    private List<PositionPointValue> computePositionPointValue(List<PlayerRank> rankings, int teams, Map<String, Long> rosterSlotsByPosition) {
        List<PositionPointValue> positionPointValues = new ArrayList<>();

        for (Map.Entry<String, Long> positionCount : rosterSlotsByPosition.entrySet()) {
            final String position = positionCount.getKey();
            if (List.of("BN","FLEX").contains(position)) continue;
            int positionRosterSlots = positionCount.getValue().intValue();
            
            BigDecimal best =
                    rankings.stream()
                    .filter(r -> r.getPosition().equals(position) && r.getPositionRank() == 1)
                    .findFirst().orElseThrow().getPointsPerGame();
            Optional<PlayerRank> first = rankings.stream()
                    .filter(r -> r.getPosition().equals(position) && r.getPositionRank() == (teams + 1))
                    .findFirst();
            if (first.isEmpty()) {
                System.out.println("Found one!");
            }
            BigDecimal replacement =
                    first.orElseThrow().getPointsPerGame(); //we want the best non-starter

            if (positionRosterSlots == 1) {
                positionPointValues.add(new PositionPointValue(position, best, replacement));
            }
            else {
                for (int i = 1; i <= positionRosterSlots; i++) {
                    var enumeratedPosition = String.format("%s%d", position, i);
                    int eye = i;

                    best = rankings.stream()
                            .filter(r -> r.getPosition().equals(position) && r.getPositionRank() == eye)
                            .findFirst().orElseThrow().getPointsPerGame();

                    replacement = rankings.stream()
                            .filter(r -> r.getPosition().equals(position) && r.getPositionRank() == ((teams * positionRosterSlots) + eye))
                            .findFirst().orElseThrow().getPointsPerGame();
                    positionPointValues.add(new PositionPointValue(enumeratedPosition, best, replacement));
                }
            }
        }
        return positionPointValues;
    }
    private List<PositionPointValue> computeFlexPointValue(List<PlayerRank> rankings, long rosterSlots) {
        List<PositionPointValue> results = new ArrayList<>();

        BigDecimal best = rankings.stream()
                .filter(r -> flexEligiblePositions.contains(r.getPosition()) && r.getFlexStartingRank() == 1)
                .findFirst().orElseThrow().getPointsPerGame();
        
        BigDecimal replacement = rankings.stream()
                .filter(r -> flexEligiblePositions.contains(r.getPosition()) && r.getNonStarterRank() == 1)
                .findFirst().orElseThrow().getPointsPerGame();
        
        if (rosterSlots == 1) {
            results.add(new PositionPointValue("FLEX", best, replacement));
        }
        else {
            for (int i = 1; i <= rosterSlots; i++) {
                var enumeratedPosition = String.format("FLEX%d", i);
                int eye = i;
                
                best = rankings.stream()
                        .filter(r -> flexEligiblePositions.contains(r.getPosition()) && r.getFlexStartingRank() == eye)
                        .findFirst().orElseThrow().getPointsPerGame();
                
                replacement = rankings.stream()
                        .filter(r -> flexEligiblePositions.contains(r.getPosition()) && r.getNonStarterRank() == eye)
                        .findFirst().orElseThrow().getPointsPerGame();
                
                results.add(new PositionPointValue(enumeratedPosition, best, replacement));
            }
        }

        return results;
    }

    private boolean isFlexStarter(int overallRank, int teams, Map<String, Long> rosterSlotsByPosition) {
        // those with an overall rank within the number of starting flex-eligible roster slots across all teams
        long slots = 0;
        for (String pos : flexEligiblePositions) {
            slots = slots + rosterSlotsByPosition.get(pos);
        }
        slots = slots * teams;
        return overallRank <= slots;
    }

    private boolean isPositionStarter(int posRank, int teams, long positionRosterSlots) {
        //position starters are those with a positional rank within the number of starting roster slots across all teams
        return posRank <= teams * positionRosterSlots;
    }


}
