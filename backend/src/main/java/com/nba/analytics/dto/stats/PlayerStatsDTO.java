package com.nba.analytics.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Player Statistics DTO
 * Aggregated player statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsDTO {
    
    private String playerId;
    private String playerName;
    private String position;
    private String teamName;
    private String teamAbbreviation;
    private String seasonId;
    
    // Scoring
    private double pointsPerGame;
    private double fieldGoalPercentage;
    private double threePointPercentage;
    private double freeThrowPercentage;
    
    // Rebounds
    private double reboundsPerGame;
    private double offensiveReboundsPerGame;
    private double defensiveReboundsPerGame;
    
    // Other stats
    private double assistsPerGame;
    private double stealsPerGame;
    private double blocksPerGame;
    private double turnoversPerGame;
    
    private int gamesPlayed;
    private double minutesPerGame;
}
