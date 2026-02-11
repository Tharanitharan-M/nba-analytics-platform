package com.nba.analytics.repository;

import com.nba.analytics.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Team Repository
 * Data access layer for Team entity
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    
    Optional<Team> findByAbbreviation(String abbreviation);
    
    List<Team> findByCity(String city);
    
    List<Team> findByState(String state);
    
    @Query("SELECT t FROM Team t WHERE LOWER(t.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(t.abbreviation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(t.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Team> searchTeams(String searchTerm);
    
    @Query("SELECT COUNT(t) FROM Team t")
    long countAllTeams();
}
