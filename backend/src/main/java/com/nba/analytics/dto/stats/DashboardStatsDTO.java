package com.nba.analytics.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard Statistics DTO
 * Overview statistics for the dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    
    private long totalGames;
    private long totalPlayers;
    private long activePlayers;
    private long totalTeams;
    private long totalPlayByPlayRecords;
    
    private String currentSeason;
    private double averagePointsPerGame;
    
    // Top performers
    private PlayerStatsDTO topScorer;
    private PlayerStatsDTO topRebounder;
    private PlayerStatsDTO topAssister;
    
    // Team standings (simplified)
    private TeamStatsDTO topTeam;
}
