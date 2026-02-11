package com.nba.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Play By Play Entity
 * Represents individual plays in NBA games
 * This table contains 13M+ rows of detailed play-by-play data
 */
@Entity
@Table(name = "play_by_play", indexes = {
    @Index(name = "idx_pbp_game_id", columnList = "game_id"),
    @Index(name = "idx_pbp_player1_id", columnList = "player1_id"),
    @Index(name = "idx_pbp_period", columnList = "period")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayByPlay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "game_id")
    private String gameId;
    
    @Column(name = "eventnum")
    private Integer eventNum;
    
    @Column(name = "eventmsgtype")
    private Integer eventMsgType;
    
    @Column(name = "eventmsgactiontype")
    private Integer eventMsgActionType;
    
    @Column(name = "period")
    private Integer period;
    
    @Column(name = "wctimestring", length = 20)
    private String wcTimeString;
    
    @Column(name = "pctimestring", length = 20)
    private String pcTimeString;
    
    @Column(name = "homedescription", columnDefinition = "TEXT")
    private String homeDescription;
    
    @Column(name = "neutraldescription", columnDefinition = "TEXT")
    private String neutralDescription;
    
    @Column(name = "visitordescription", columnDefinition = "TEXT")
    private String visitorDescription;
    
    @Column(name = "score", length = 20)
    private String score;
    
    @Column(name = "scoremargin", length = 10)
    private String scoreMargin;
    
    // Player 1 information
    @Column(name = "person1type")
    private Double person1Type;
    
    @Column(name = "player1_id")
    private String player1Id;
    
    @Column(name = "player1_name", length = 100)
    private String player1Name;
    
    @Column(name = "player1_team_id")
    private String player1TeamId;
    
    @Column(name = "player1_team_city", length = 50)
    private String player1TeamCity;
    
    @Column(name = "player1_team_nickname", length = 50)
    private String player1TeamNickname;
    
    @Column(name = "player1_team_abbreviation", length = 10)
    private String player1TeamAbbreviation;
    
    // Player 2 information
    @Column(name = "person2type")
    private Double person2Type;
    
    @Column(name = "player2_id")
    private String player2Id;
    
    @Column(name = "player2_name", length = 100)
    private String player2Name;
    
    @Column(name = "player2_team_id")
    private String player2TeamId;
    
    @Column(name = "player2_team_city", length = 50)
    private String player2TeamCity;
    
    @Column(name = "player2_team_nickname", length = 50)
    private String player2TeamNickname;
    
    @Column(name = "player2_team_abbreviation", length = 10)
    private String player2TeamAbbreviation;
    
    // Player 3 information
    @Column(name = "person3type")
    private Double person3Type;
    
    @Column(name = "player3_id")
    private String player3Id;
    
    @Column(name = "player3_name", length = 100)
    private String player3Name;
    
    @Column(name = "player3_team_id")
    private String player3TeamId;
    
    @Column(name = "player3_team_city", length = 50)
    private String player3TeamCity;
    
    @Column(name = "player3_team_nickname", length = 50)
    private String player3TeamNickname;
    
    @Column(name = "player3_team_abbreviation", length = 10)
    private String player3TeamAbbreviation;
}
