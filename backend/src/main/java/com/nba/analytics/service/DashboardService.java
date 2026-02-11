package com.nba.analytics.service;

import com.nba.analytics.dto.stats.DashboardStatsDTO;
import com.nba.analytics.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Dashboard Service
 * Provides aggregated statistics for the dashboard
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private PlayByPlayRepository playByPlayRepository;
    
    /**
     * Get dashboard overview statistics
     */
    public DashboardStatsDTO getDashboardStats() {
        long totalGames = gameRepository.count();
        long totalPlayers = playerRepository.count();
        long activePlayers = playerRepository.countActivePlayers();
        long totalTeams = teamRepository.count();
        long totalPlayByPlay = playByPlayRepository.count();
        
        // Get current season
        List<String> seasons = gameRepository.findAllSeasons();
        String currentSeason = seasons.isEmpty() ? "N/A" : seasons.get(0);
        
        // Get average points per game for current season
        Double avgPoints = gameRepository.getAveragePointsPerGame(currentSeason);
        
        return DashboardStatsDTO.builder()
                .totalGames(totalGames)
                .totalPlayers(totalPlayers)
                .activePlayers(activePlayers)
                .totalTeams(totalTeams)
                .totalPlayByPlayRecords(totalPlayByPlay)
                .currentSeason(currentSeason)
                .averagePointsPerGame(avgPoints != null ? avgPoints : 0.0)
                .build();
    }
}
