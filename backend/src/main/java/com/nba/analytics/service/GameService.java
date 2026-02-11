package com.nba.analytics.service;

import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.exception.ResourceNotFoundException;
import com.nba.analytics.model.Game;
import com.nba.analytics.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Game Service
 * Business logic for game operations
 */
@Service
@Transactional(readOnly = true)
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    
    public Game getGameById(String gameId) {
        return gameRepository.findByGameId(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + gameId));
    }
    
    public PageResponse<Game> getRecentGames(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "gameDate"));
        Page<Game> gamePage = gameRepository.findRecentGames(pageable);
        
        return buildPageResponse(gamePage);
    }
    
    public PageResponse<Game> getGamesBySeason(String seasonId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "gameDate"));
        Page<Game> gamePage = gameRepository.findBySeasonId(seasonId, pageable);
        
        return buildPageResponse(gamePage);
    }
    
    public PageResponse<Game> getGamesByTeam(String teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> gamePage = gameRepository.findGamesByTeam(teamId, pageable);
        
        return buildPageResponse(gamePage);
    }
    
    public PageResponse<Game> getGamesByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> gamePage = gameRepository.findGamesByDateRange(startDateTime, endDateTime, pageable);
        
        return buildPageResponse(gamePage);
    }
    
    /**
     * Get games that occurred on this day in history
     */
    public List<Game> getGamesOnThisDay(int month, int day) {
        return gameRepository.findGamesOnThisDay(month, day);
    }
    
    public List<String> getAllSeasons() {
        return gameRepository.findAllSeasons();
    }
    
    public long getTotalGamesCount() {
        return gameRepository.count();
    }
    
    public Double getAveragePointsPerGame(String seasonId) {
        return gameRepository.getAveragePointsPerGame(seasonId);
    }
    
    public List<Game> getHighScoringGames(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return gameRepository.findHighScoringGames(pageable);
    }
    
    private PageResponse<Game> buildPageResponse(Page<Game> gamePage) {
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
}
