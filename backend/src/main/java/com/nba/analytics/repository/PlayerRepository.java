package com.nba.analytics.repository;

import com.nba.analytics.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Player Repository
 * Data access layer for Player entity
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    
    List<Player> findByIsActive(Integer isActive);
    
    Page<Player> findByIsActive(Integer isActive, Pageable pageable);
    
    @Query("SELECT p FROM Player p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Player> searchPlayers(String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Player p WHERE p.isActive = 1")
    long countActivePlayers();
    
    @Query("SELECT COUNT(p) FROM Player p")
    long countAllPlayers();
}
