package dev.tk2575.fantasysports.core.nfl;

public interface PlayerRankingInterface extends PlayerProjectionInterface {
    
    int getOverallRank(); //rank across all positions
    int getPositionRank(); //rank across player's position
    int getFlexRank(); //rank across all flex positions
        
}
