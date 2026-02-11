package com.nba.analytics.service;

import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.dto.stats.TeamStatsDTO;
import com.nba.analytics.exception.ResourceNotFoundException;
import com.nba.analytics.model.Game;
import com.nba.analytics.model.Team;
import com.nba.analytics.repository.GameRepository;
import com.nba.analytics.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Team Service
 * Business logic for team operations
 */
@Service
@Transactional(readOnly = true)
public class TeamService {
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    public List<Team> getAllTeams() {
        return teamRepository.findAll(Sort.by(Sort.Direction.ASC, "fullName"));
    }
    
    public Team getTeamById(String teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
    }
    
    public Team getTeamByAbbreviation(String abbreviation) {
        return teamRepository.findByAbbreviation(abbreviation)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with abbreviation: " + abbreviation));
    }
    
    public List<Team> searchTeams(String searchTerm) {
        return teamRepository.searchTeams(searchTerm);
    }
    
    public PageResponse<Game> getTeamGames(String teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "gameDate"));
        Page<Game> gamePage = gameRepository.findGamesByTeam(teamId, pageable);
        
        return PageResponse.<Game>builder()
                .content(gamePage.getContent())
                .pageNumber(gamePage.getNumber())
                .pageSize(gamePage.getSize())
                .totalElements(gamePage.getTotalElements())
                .totalPages(gamePage.getTotalPages())
                .last(gamePage.isLast())
                .first(gamePage.isFirst())
                .build();
    }
    
    /**
     * Calculate team statistics for a given season
     */
    public TeamStatsDTO getTeamStatsBySeason(String teamId, String seasonId) {
        Team team = getTeamById(teamId);
        List<Game> games = gameRepository.findGamesByTeamAndSeason(teamId, seasonId);
        
        if (games.isEmpty()) {
            throw new ResourceNotFoundException("No games found for team " + teamId + " in season " + seasonId);
        }
        
        int wins = 0;
        int losses = 0;
        double totalPoints = 0;
        double totalPointsAllowed = 0;
        double totalFgPct = 0;
        double total3PtPct = 0;
        double totalFtPct = 0;
        double totalReb = 0;
        double totalAst = 0;
        double totalStl = 0;
        double totalBlk = 0;
        double totalTov = 0;
        
        for (Game game : games) {
            boolean isHome = game.getTeamIdHome().equals(teamId);
            
            if (isHome) {
                if ("W".equals(game.getWlHome())) wins++;
                else losses++;
                
                totalPoints += game.getPtsHome() != null ? game.getPtsHome() : 0;
                totalPointsAllowed += game.getPtsAway() != null ? game.getPtsAway() : 0;
                totalFgPct += game.getFgPctHome() != null ? game.getFgPctHome() : 0;
                total3PtPct += game.getFg3PctHome() != null ? game.getFg3PctHome() : 0;
                totalFtPct += game.getFtPctHome() != null ? game.getFtPctHome() : 0;
                totalReb += game.getRebHome() != null ? game.getRebHome() : 0;
                totalAst += game.getAstHome() != null ? game.getAstHome() : 0;
                totalStl += game.getStlHome() != null ? game.getStlHome() : 0;
                totalBlk += game.getBlkHome() != null ? game.getBlkHome() : 0;
                totalTov += game.getTovHome() != null ? game.getTovHome() : 0;
            } else {
                if ("W".equals(game.getWlAway())) wins++;
                else losses++;
                
                totalPoints += game.getPtsAway() != null ? game.getPtsAway() : 0;
                totalPointsAllowed += game.getPtsHome() != null ? game.getPtsHome() : 0;
                totalFgPct += game.getFgPctAway() != null ? game.getFgPctAway() : 0;
                total3PtPct += game.getFg3PctAway() != null ? game.getFg3PctAway() : 0;
                totalFtPct += game.getFtPctAway() != null ? game.getFtPctAway() : 0;
                totalReb += game.getRebAway() != null ? game.getRebAway() : 0;
                totalAst += game.getAstAway() != null ? game.getAstAway() : 0;
                totalStl += game.getStlAway() != null ? game.getStlAway() : 0;
                totalBlk += game.getBlkAway() != null ? game.getBlkAway() : 0;
                totalTov += game.getTovAway() != null ? game.getTovAway() : 0;
            }
        }
        
        int gamesPlayed = games.size();
        double winPct = (double) wins / gamesPlayed;
        
        return TeamStatsDTO.builder()
                .teamId(teamId)
                .teamName(team.getFullName())
                .abbreviation(team.getAbbreviation())
                .seasonId(seasonId)
                .wins(wins)
                .losses(losses)
                .winPercentage(winPct)
                .pointsPerGame(totalPoints / gamesPlayed)
                .pointsAllowedPerGame(totalPointsAllowed / gamesPlayed)
                .pointsDifferential((totalPoints - totalPointsAllowed) / gamesPlayed)
                .fieldGoalPercentage(totalFgPct / gamesPlayed)
                .threePointPercentage(total3PtPct / gamesPlayed)
                .freeThrowPercentage(totalFtPct / gamesPlayed)
                .reboundsPerGame(totalReb / gamesPlayed)
                .assistsPerGame(totalAst / gamesPlayed)
                .stealsPerGame(totalStl / gamesPlayed)
                .blocksPerGame(totalBlk / gamesPlayed)
                .turnoversPerGame(totalTov / gamesPlayed)
                .gamesPlayed(gamesPlayed)
                .build();
    }
    
    public long getTotalTeamsCount() {
        return teamRepository.count();
    }
}
