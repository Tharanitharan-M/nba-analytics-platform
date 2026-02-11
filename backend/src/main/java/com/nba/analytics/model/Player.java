package com.nba.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Player Entity
 * Represents an NBA player with basic information
 */
@Entity
@Table(name = "player", indexes = {
    @Index(name = "idx_player_full_name", columnList = "full_name"),
    @Index(name = "idx_player_is_active", columnList = "is_active")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "is_active")
    private Integer isActive;
}
