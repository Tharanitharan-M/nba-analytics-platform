package com.nba.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Team Entity
 * Represents an NBA team with basic information
 */
@Entity
@Table(name = "team", indexes = {
    @Index(name = "idx_team_abbreviation", columnList = "abbreviation"),
    @Index(name = "idx_team_full_name", columnList = "full_name")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "abbreviation", length = 10)
    private String abbreviation;
    
    @Column(name = "nickname", length = 50)
    private String nickname;
    
    @Column(name = "city", length = 50)
    private String city;
    
    @Column(name = "state", length = 50)
    private String state;
    
    @Column(name = "year_founded")
    private Double yearFounded;
}
