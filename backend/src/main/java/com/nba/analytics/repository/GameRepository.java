package com.nba.analytics.repository;

import com.nba.analytics.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Game Repository
 * Data access layer for Game entity with advanced queries
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    Optional<Game> findByGameId(String gameId);
    
    List<Game> findBySeasonId(String seasonId);
    
    Page<Game> findBySeasonId(String seasonId, Pageable pageable);
    
    @Query("SELECT g FROM Game g WHERE g.teamIdHome = :teamId OR g.teamIdAway = :teamId ORDER BY g.gameDate DESC")
    Page<Game> findGamesByTeam(@Param("teamId") String teamId, Pageable pageable);
    
    @Query("SELECT g FROM Game g WHERE (g.teamIdHome = :teamId OR g.teamIdAway = :teamId) AND g.seasonId = :seasonId ORDER BY g.gameDate DESC")
    List<Game> findGamesByTeamAndSeason(@Param("teamId") String teamId, @Param("seasonId") String seasonId);
    
    @Query("SELECT g FROM Game g WHERE g.gameDate >= :startDate AND g.gameDate <= :endDate ORDER BY g.gameDate DESC")
    Page<Game> findGamesByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate, 
                                     Pageable pageable);
    
    @Query("SELECT g FROM Game g WHERE MONTH(g.gameDate) = :month AND DAY(g.gameDate) = :day ORDER BY g.gameDate DESC")
    List<Game> findGamesOnThisDay(@Param("month") int month, @Param("day") int day);
    
    @Query("SELECT g FROM Game g ORDER BY g.gameDate DESC")
    Page<Game> findRecentGames(Pageable pageable);
    
    @Query("SELECT COUNT(g) FROM Game g")
    long countAllGames();
    
    @Query("SELECT COUNT(g) FROM Game g WHERE g.seasonId = :seasonId")
    long countGamesBySeason(@Param("seasonId") String seasonId);
    
    @Query("SELECT DISTINCT g.seasonId FROM Game g ORDER BY g.seasonId DESC")
    List<String> findAllSeasons();
    
    // Advanced analytics queries
    @Query("SELECT AVG(g.ptsHome + g.ptsAway) FROM Game g WHERE g.seasonId = :seasonId")
    Double getAveragePointsPerGame(@Param("seasonId") String seasonId);
    
    @Query("SELECT g FROM Game g WHERE (g.ptsHome > 150 OR g.ptsAway > 150) ORDER BY GREATEST(g.ptsHome, g.ptsAway) DESC")
    List<Game> findHighScoringGames(Pageable pageable);
}
