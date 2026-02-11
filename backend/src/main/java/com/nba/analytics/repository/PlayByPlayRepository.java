package com.nba.analytics.repository;

import com.nba.analytics.model.PlayByPlay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Play By Play Repository
 * Data access layer for Play-by-Play data (13M+ rows)
 */
@Repository
public interface PlayByPlayRepository extends JpaRepository<PlayByPlay, Long> {
    
    List<PlayByPlay> findByGameId(String gameId);
    
    Page<PlayByPlay> findByGameId(String gameId, Pageable pageable);
    
    @Query("SELECT p FROM PlayByPlay p WHERE p.gameId = :gameId ORDER BY p.period, p.eventNum")
    List<PlayByPlay> findByGameIdOrdered(@Param("gameId") String gameId);
    
    @Query("SELECT p FROM PlayByPlay p WHERE p.player1Id = :playerId OR p.player2Id = :playerId OR p.player3Id = :playerId")
    Page<PlayByPlay> findPlaysByPlayer(@Param("playerId") String playerId, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM PlayByPlay p WHERE p.gameId = :gameId")
    long countPlaysByGame(@Param("gameId") String gameId);
    
    @Query("SELECT COUNT(p) FROM PlayByPlay p")
    long countAllPlays();
}
