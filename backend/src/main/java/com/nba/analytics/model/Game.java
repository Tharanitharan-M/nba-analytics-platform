package com.nba.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Game Entity
 * Represents an NBA game with box score statistics for both teams
 */
@Entity
@Table(name = "game", indexes = {
    @Index(name = "idx_game_id", columnList = "game_id"),
    @Index(name = "idx_game_date", columnList = "game_date"),
    @Index(name = "idx_season_id", columnList = "season_id"),
    @Index(name = "idx_team_home", columnList = "team_id_home"),
    @Index(name = "idx_team_away", columnList = "team_id_away")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "season_id", length = 20)
    private String seasonId;
    
    @Column(name = "team_id_home")
    private String teamIdHome;
    
    @Column(name = "team_abbreviation_home", length = 10)
    private String teamAbbreviationHome;
    
    @Column(name = "team_name_home", length = 100)
    private String teamNameHome;
    
    @Column(name = "game_id", unique = true)
    private String gameId;
    
    @Column(name = "game_date")
    private LocalDateTime gameDate;
    
    @Column(name = "matchup_home", length = 50)
    private String matchupHome;
    
    @Column(name = "wl_home", length = 5)
    private String wlHome;
    
    @Column(name = "min")
    private Integer min;
    
    // Home team statistics
    @Column(name = "fgm_home")
    private Double fgmHome;
    
    @Column(name = "fga_home")
    private Double fgaHome;
    
    @Column(name = "fg_pct_home")
    private Double fgPctHome;
    
    @Column(name = "fg3m_home")
    private Double fg3mHome;
    
    @Column(name = "fg3a_home")
    private Double fg3aHome;
    
    @Column(name = "fg3_pct_home")
    private Double fg3PctHome;
    
    @Column(name = "ftm_home")
    private Double ftmHome;
    
    @Column(name = "fta_home")
    private Double ftaHome;
    
    @Column(name = "ft_pct_home")
    private Double ftPctHome;
    
    @Column(name = "oreb_home")
    private Double orebHome;
    
    @Column(name = "dreb_home")
    private Double drebHome;
    
    @Column(name = "reb_home")
    private Double rebHome;
    
    @Column(name = "ast_home")
    private Double astHome;
    
    @Column(name = "stl_home")
    private Double stlHome;
    
    @Column(name = "blk_home")
    private Double blkHome;
    
    @Column(name = "tov_home")
    private Double tovHome;
    
    @Column(name = "pf_home")
    private Double pfHome;
    
    @Column(name = "pts_home")
    private Double ptsHome;
    
    @Column(name = "plus_minus_home")
    private Integer plusMinusHome;
    
    @Column(name = "video_available_home")
    private Integer videoAvailableHome;
    
    // Away team information and statistics
    @Column(name = "team_id_away")
    private String teamIdAway;
    
    @Column(name = "team_abbreviation_away", length = 10)
    private String teamAbbreviationAway;
    
    @Column(name = "team_name_away", length = 100)
    private String teamNameAway;
    
    @Column(name = "matchup_away", length = 50)
    private String matchupAway;
    
    @Column(name = "wl_away", length = 5)
    private String wlAway;
    
    @Column(name = "fgm_away")
    private Double fgmAway;
    
    @Column(name = "fga_away")
    private Double fgaAway;
    
    @Column(name = "fg_pct_away")
    private Double fgPctAway;
    
    @Column(name = "fg3m_away")
    private Double fg3mAway;
    
    @Column(name = "fg3a_away")
    private Double fg3aAway;
    
    @Column(name = "fg3_pct_away")
    private Double fg3PctAway;
    
    @Column(name = "ftm_away")
    private Double ftmAway;
    
    @Column(name = "fta_away")
    private Double ftaAway;
    
    @Column(name = "ft_pct_away")
    private Double ftPctAway;
    
    @Column(name = "oreb_away")
    private Double orebAway;
    
    @Column(name = "dreb_away")
    private Double drebAway;
    
    @Column(name = "reb_away")
    private Double rebAway;
    
    @Column(name = "ast_away")
    private Double astAway;
    
    @Column(name = "stl_away")
    private Double stlAway;
    
    @Column(name = "blk_away")
    private Double blkAway;
    
    @Column(name = "tov_away")
    private Double tovAway;
    
    @Column(name = "pf_away")
    private Double pfAway;
    
    @Column(name = "pts_away")
    private Double ptsAway;
    
    @Column(name = "plus_minus_away")
    private Integer plusMinusAway;
    
    @Column(name = "video_available_away")
    private Integer videoAvailableAway;
}
