package com.nba.analytics.repository;

import com.nba.analytics.model.CommonPlayerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Common Player Info Repository
 * Data access layer for detailed player information
 */
@Repository
public interface CommonPlayerInfoRepository extends JpaRepository<CommonPlayerInfo, String> {
    
    List<CommonPlayerInfo> findByTeamId(Integer teamId);
    
    List<CommonPlayerInfo> findByPosition(String position);
    
    List<CommonPlayerInfo> findByCountry(String country);
    
    @Query("SELECT c FROM CommonPlayerInfo c WHERE c.rosterStatus = 'Active'")
    List<CommonPlayerInfo> findActivePlayers();
    
    @Query("SELECT c FROM CommonPlayerInfo c WHERE c.teamId = :teamId AND c.rosterStatus = 'Active'")
    List<CommonPlayerInfo> findActivePlayersByTeam(Integer teamId);
    
    @Query("SELECT DISTINCT c.position FROM CommonPlayerInfo c WHERE c.position IS NOT NULL ORDER BY c.position")
    List<String> findAllPositions();
    
    @Query("SELECT DISTINCT c.country FROM CommonPlayerInfo c WHERE c.country IS NOT NULL ORDER BY c.country")
    List<String> findAllCountries();
    
    Page<CommonPlayerInfo> findByTeamId(Integer teamId, Pageable pageable);
}
