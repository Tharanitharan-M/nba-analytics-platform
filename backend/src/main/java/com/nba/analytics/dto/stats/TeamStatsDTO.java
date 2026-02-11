package com.nba.analytics.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Team Statistics DTO
 * Aggregated team statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamStatsDTO {
    
    private String teamId;
    private String teamName;
    private String abbreviation;
    private String seasonId;
    
    // Win/Loss record
    private int wins;
    private int losses;
    private double winPercentage;
    
    // Scoring statistics
    private double pointsPerGame;
    private double pointsAllowedPerGame;
    private double pointsDifferential;
    
    // Shooting statistics
    private double fieldGoalPercentage;
    private double threePointPercentage;
    private double freeThrowPercentage;
    
    // Other statistics
    private double reboundsPerGame;
    private double assistsPerGame;
    private double stealsPerGame;
    private double blocksPerGame;
    private double turnoversPerGame;
    
    private int gamesPlayed;
}
