package com.nba.analytics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Common Player Info Entity
 * Contains detailed player information including physical stats and career details
 */
@Entity
@Table(name = "common_player_info", indexes = {
    @Index(name = "idx_cpi_person_id", columnList = "person_id"),
    @Index(name = "idx_cpi_team_id", columnList = "team_id"),
    @Index(name = "idx_cpi_display_name", columnList = "display_first_last")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonPlayerInfo {
    
    @Id
    @Column(name = "person_id", nullable = false)
    private String personId;
    
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Column(name = "display_first_last", length = 100)
    private String displayFirstLast;
    
    @Column(name = "display_last_comma_first", length = 100)
    private String displayLastCommaFirst;
    
    @Column(name = "display_fi_last", length = 100)
    private String displayFiLast;
    
    @Column(name = "player_slug", length = 100)
    private String playerSlug;
    
    @Column(name = "birthdate")
    private LocalDateTime birthdate;
    
    @Column(name = "school", length = 100)
    private String school;
    
    @Column(name = "country", length = 50)
    private String country;
    
    @Column(name = "last_affiliation", length = 100)
    private String lastAffiliation;
    
    @Column(name = "height", length = 20)
    private String height;
    
    @Column(name = "weight", length = 20)
    private String weight;
    
    @Column(name = "season_exp")
    private Double seasonExp;
    
    @Column(name = "jersey", length = 20)
    private String jersey;
    
    @Column(name = "position", length = 20)
    private String position;
    
    @Column(name = "rosterstatus", length = 20)
    private String rosterStatus;
    
    @Column(name = "games_played_current_season_flag", length = 5)
    private String gamesPlayedCurrentSeasonFlag;
    
    @Column(name = "team_id")
    private Integer teamId;
    
    @Column(name = "team_name", length = 100)
    private String teamName;
    
    @Column(name = "team_abbreviation", length = 20)
    private String teamAbbreviation;
    
    @Column(name = "team_code", length = 50)
    private String teamCode;
    
    @Column(name = "team_city", length = 50)
    private String teamCity;
    
    @Column(name = "playercode", length = 50)
    private String playerCode;
    
    @Column(name = "from_year")
    private Double fromYear;
    
    @Column(name = "to_year")
    private Double toYear;
    
    @Column(name = "dleague_flag", length = 5)
    private String dleagueFlag;
    
    @Column(name = "nba_flag", length = 5)
    private String nbaFlag;
    
    @Column(name = "games_played_flag", length = 5)
    private String gamesPlayedFlag;
    
    @Column(name = "draft_year", length = 20)
    private String draftYear;
    
    @Column(name = "draft_round", length = 20)
    private String draftRound;
    
    @Column(name = "draft_number", length = 20)
    private String draftNumber;
}
