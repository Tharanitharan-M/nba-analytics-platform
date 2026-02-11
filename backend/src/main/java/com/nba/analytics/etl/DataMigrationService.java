package com.nba.analytics.etl;

import com.nba.analytics.model.*;
import com.nba.analytics.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Migration Service
 * ETL Pipeline: Extracts data from SQLite and loads into PostgreSQL
 * 
 * This is a key data engineering component that:
 * - Extracts data from the source SQLite database
 * - Transforms data types and formats as needed
 * - Loads data into PostgreSQL with batch processing
 * - Handles data quality and error management
 */
@Service
public class DataMigrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataMigrationService.class);
    private static final int BATCH_SIZE = 1000;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private CommonPlayerInfoRepository commonPlayerInfoRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private PlayByPlayRepository playByPlayRepository;
    
    /**
     * Main migration method - orchestrates the entire ETL process
     */
    public void migrateAllData(String sqlitePath) {
        logger.info("🚀 Starting NBA Data Migration Pipeline");
        logger.info("Source: SQLite database at {}", sqlitePath);
        logger.info("Destination: PostgreSQL");
        
        long startTime = System.currentTimeMillis();
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + sqlitePath)) {
            logger.info("✅ Connected to SQLite database");
            
            // ETL Pipeline stages
            migrateTeams(conn);
            migratePlayers(conn);
            migrateCommonPlayerInfo(conn);
            migrateGames(conn);
            migratePlayByPlay(conn);
            
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000;
            
            logger.info("✅ Migration completed successfully in {} seconds", duration);
            logger.info("📊 Data Engineering Pipeline Statistics:");
            logger.info("   - Teams: {}", teamRepository.count());
            logger.info("   - Players: {}", playerRepository.count());
            logger.info("   - Player Details: {}", commonPlayerInfoRepository.count());
            logger.info("   - Games: {}", gameRepository.count());
            logger.info("   - Play-by-Play Records: {}", playByPlayRepository.count());
            
        } catch (SQLException e) {
            logger.error("❌ Migration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Data migration failed", e);
        }
    }
    
    /**
     * Extract and Load Teams
     */
    @Transactional
    public void migrateTeams(Connection conn) throws SQLException {
        logger.info("📦 Extracting Teams...");
        String sql = "SELECT * FROM team";
        
        List<Team> teams = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getString("id"));
                team.setFullName(rs.getString("full_name"));
                team.setAbbreviation(rs.getString("abbreviation"));
                team.setNickname(rs.getString("nickname"));
                team.setCity(rs.getString("city"));
                team.setState(rs.getString("state"));
                team.setYearFounded(rs.getDouble("year_founded"));
                teams.add(team);
            }
        }
        
        logger.info("💾 Loading {} teams into PostgreSQL...", teams.size());
        teamRepository.saveAll(teams);
        logger.info("✅ Teams migration complete");
    }
    
    /**
     * Extract and Load Players
     */
    @Transactional
    public void migratePlayers(Connection conn) throws SQLException {
        logger.info("📦 Extracting Players...");
        String sql = "SELECT * FROM player";
        
        List<Player> players = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Player player = new Player();
                player.setId(rs.getString("id"));
                player.setFullName(rs.getString("full_name"));
                player.setFirstName(rs.getString("first_name"));
                player.setLastName(rs.getString("last_name"));
                player.setIsActive(rs.getInt("is_active"));
                players.add(player);
            }
        }
        
        logger.info("💾 Loading {} players into PostgreSQL...", players.size());
        playerRepository.saveAll(players);
        logger.info("✅ Players migration complete");
    }
    
    /**
     * Extract and Load Common Player Info with batch processing
     */
    @Transactional
    public void migrateCommonPlayerInfo(Connection conn) throws SQLException {
        logger.info("📦 Extracting Common Player Info...");
        String sql = "SELECT * FROM common_player_info";
        
        List<CommonPlayerInfo> playerInfos = new ArrayList<>();
        int count = 0;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                CommonPlayerInfo info = new CommonPlayerInfo();
                info.setPersonId(rs.getString("person_id"));
                info.setFirstName(rs.getString("first_name"));
                info.setLastName(rs.getString("last_name"));
                info.setDisplayFirstLast(rs.getString("display_first_last"));
                info.setDisplayLastCommaFirst(rs.getString("display_last_comma_first"));
                info.setDisplayFiLast(rs.getString("display_fi_last"));
                info.setPlayerSlug(rs.getString("player_slug"));
                
                // Handle timestamp conversion
                String birthdate = rs.getString("birthdate");
                if (birthdate != null && !birthdate.isEmpty()) {
                    try {
                        info.setBirthdate(LocalDateTime.parse(birthdate.replace(" ", "T")));
                    } catch (Exception e) {
                        // Skip invalid dates
                    }
                }
                
                info.setSchool(rs.getString("school"));
                info.setCountry(rs.getString("country"));
                info.setLastAffiliation(rs.getString("last_affiliation"));
                info.setHeight(rs.getString("height"));
                info.setWeight(rs.getString("weight"));
                info.setSeasonExp(getNullableDouble(rs, "season_exp"));
                info.setJersey(rs.getString("jersey"));
                info.setPosition(rs.getString("position"));
                info.setRosterStatus(rs.getString("rosterstatus"));
                info.setGamesPlayedCurrentSeasonFlag(rs.getString("games_played_current_season_flag"));
                info.setTeamId(getNullableInt(rs, "team_id"));
                info.setTeamName(rs.getString("team_name"));
                info.setTeamAbbreviation(rs.getString("team_abbreviation"));
                info.setTeamCode(rs.getString("team_code"));
                info.setTeamCity(rs.getString("team_city"));
                info.setPlayerCode(rs.getString("playercode"));
                info.setFromYear(getNullableDouble(rs, "from_year"));
                info.setToYear(getNullableDouble(rs, "to_year"));
                info.setDleagueFlag(rs.getString("dleague_flag"));
                info.setNbaFlag(rs.getString("nba_flag"));
                info.setGamesPlayedFlag(rs.getString("games_played_flag"));
                info.setDraftYear(rs.getString("draft_year"));
                info.setDraftRound(rs.getString("draft_round"));
                info.setDraftNumber(rs.getString("draft_number"));
                
                playerInfos.add(info);
                count++;
                
                // Batch processing
                if (playerInfos.size() >= BATCH_SIZE) {
                    commonPlayerInfoRepository.saveAll(playerInfos);
                    logger.info("💾 Loaded {} player info records...", count);
                    playerInfos.clear();
                }
            }
            
            // Save remaining
            if (!playerInfos.isEmpty()) {
                commonPlayerInfoRepository.saveAll(playerInfos);
            }
        }
        
        logger.info("✅ Common Player Info migration complete: {} records", count);
    }
    
    /**
     * Extract and Load Games with batch processing
     */
    @Transactional
    public void migrateGames(Connection conn) throws SQLException {
        logger.info("📦 Extracting Games (this may take a while - 65K+ games)...");
        String sql = "SELECT * FROM game";
        
        List<Game> games = new ArrayList<>();
        int count = 0;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Game game = new Game();
                game.setSeasonId(rs.getString("season_id"));
                game.setTeamIdHome(rs.getString("team_id_home"));
                game.setTeamAbbreviationHome(rs.getString("team_abbreviation_home"));
                game.setTeamNameHome(rs.getString("team_name_home"));
                game.setGameId(rs.getString("game_id"));
                
                // Handle timestamp
                String gameDate = rs.getString("game_date");
                if (gameDate != null && !gameDate.isEmpty()) {
                    try {
                        game.setGameDate(LocalDateTime.parse(gameDate.replace(" ", "T")));
                    } catch (Exception e) {
                        // Skip invalid dates
                    }
                }
                
                game.setMatchupHome(rs.getString("matchup_home"));
                game.setWlHome(rs.getString("wl_home"));
                game.setMin(getNullableInt(rs, "min"));
                
                // Home team stats
                game.setFgmHome(getNullableDouble(rs, "fgm_home"));
                game.setFgaHome(getNullableDouble(rs, "fga_home"));
                game.setFgPctHome(getNullableDouble(rs, "fg_pct_home"));
                game.setFg3mHome(getNullableDouble(rs, "fg3m_home"));
                game.setFg3aHome(getNullableDouble(rs, "fg3a_home"));
                game.setFg3PctHome(getNullableDouble(rs, "fg3_pct_home"));
                game.setFtmHome(getNullableDouble(rs, "ftm_home"));
                game.setFtaHome(getNullableDouble(rs, "fta_home"));
                game.setFtPctHome(getNullableDouble(rs, "ft_pct_home"));
                game.setOrebHome(getNullableDouble(rs, "oreb_home"));
                game.setDrebHome(getNullableDouble(rs, "dreb_home"));
                game.setRebHome(getNullableDouble(rs, "reb_home"));
                game.setAstHome(getNullableDouble(rs, "ast_home"));
                game.setStlHome(getNullableDouble(rs, "stl_home"));
                game.setBlkHome(getNullableDouble(rs, "blk_home"));
                game.setTovHome(getNullableDouble(rs, "tov_home"));
                game.setPfHome(getNullableDouble(rs, "pf_home"));
                game.setPtsHome(getNullableDouble(rs, "pts_home"));
                game.setPlusMinusHome(getNullableInt(rs, "plus_minus_home"));
                game.setVideoAvailableHome(getNullableInt(rs, "video_available_home"));
                
                // Away team stats
                game.setTeamIdAway(rs.getString("team_id_away"));
                game.setTeamAbbreviationAway(rs.getString("team_abbreviation_away"));
                game.setTeamNameAway(rs.getString("team_name_away"));
                game.setMatchupAway(rs.getString("matchup_away"));
                game.setWlAway(rs.getString("wl_away"));
                game.setFgmAway(getNullableDouble(rs, "fgm_away"));
                game.setFgaAway(getNullableDouble(rs, "fga_away"));
                game.setFgPctAway(getNullableDouble(rs, "fg_pct_away"));
                game.setFg3mAway(getNullableDouble(rs, "fg3m_away"));
                game.setFg3aAway(getNullableDouble(rs, "fg3a_away"));
                game.setFg3PctAway(getNullableDouble(rs, "fg3_pct_away"));
                game.setFtmAway(getNullableDouble(rs, "ftm_away"));
                game.setFtaAway(getNullableDouble(rs, "fta_away"));
                game.setFtPctAway(getNullableDouble(rs, "ft_pct_away"));
                game.setOrebAway(getNullableDouble(rs, "oreb_away"));
                game.setDrebAway(getNullableDouble(rs, "dreb_away"));
                game.setRebAway(getNullableDouble(rs, "reb_away"));
                game.setAstAway(getNullableDouble(rs, "ast_away"));
                game.setStlAway(getNullableDouble(rs, "stl_away"));
                game.setBlkAway(getNullableDouble(rs, "blk_away"));
                game.setTovAway(getNullableDouble(rs, "tov_away"));
                game.setPfAway(getNullableDouble(rs, "pf_away"));
                game.setPtsAway(getNullableDouble(rs, "pts_away"));
                game.setPlusMinusAway(getNullableInt(rs, "plus_minus_away"));
                game.setVideoAvailableAway(getNullableInt(rs, "video_available_away"));
                
                games.add(game);
                count++;
                
                // Batch processing
                if (games.size() >= BATCH_SIZE) {
                    gameRepository.saveAll(games);
                    logger.info("💾 Loaded {} games...", count);
                    games.clear();
                }
            }
            
            // Save remaining
            if (!games.isEmpty()) {
                gameRepository.saveAll(games);
            }
        }
        
        logger.info("✅ Games migration complete: {} records", count);
    }
    
    /**
     * Extract and Load Play-by-Play data
     * This is the largest dataset with 13M+ rows
     */
    @Transactional
    public void migratePlayByPlay(Connection conn) throws SQLException {
        logger.info("📦 Extracting Play-by-Play data (13M+ records - this will take several minutes)...");
        logger.info("⚠️  Note: For production, consider using bulk copy or parallel processing");
        
        String sql = "SELECT * FROM play_by_play LIMIT 50000"; // Limit for demo purposes
        
        List<PlayByPlay> plays = new ArrayList<>();
        int count = 0;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                PlayByPlay play = new PlayByPlay();
                play.setGameId(rs.getString("game_id"));
                play.setEventNum(getNullableInt(rs, "eventnum"));
                play.setEventMsgType(getNullableInt(rs, "eventmsgtype"));
                play.setEventMsgActionType(getNullableInt(rs, "eventmsgactiontype"));
                play.setPeriod(getNullableInt(rs, "period"));
                play.setWcTimeString(rs.getString("wctimestring"));
                play.setPcTimeString(rs.getString("pctimestring"));
                play.setHomeDescription(rs.getString("homedescription"));
                play.setNeutralDescription(rs.getString("neutraldescription"));
                play.setVisitorDescription(rs.getString("visitordescription"));
                play.setScore(rs.getString("score"));
                play.setScoreMargin(rs.getString("scoremargin"));
                
                // Player 1
                play.setPerson1Type(getNullableDouble(rs, "person1type"));
                play.setPlayer1Id(rs.getString("player1_id"));
                play.setPlayer1Name(rs.getString("player1_name"));
                play.setPlayer1TeamId(rs.getString("player1_team_id"));
                play.setPlayer1TeamCity(rs.getString("player1_team_city"));
                play.setPlayer1TeamNickname(rs.getString("player1_team_nickname"));
                play.setPlayer1TeamAbbreviation(rs.getString("player1_team_abbreviation"));
                
                // Player 2
                play.setPerson2Type(getNullableDouble(rs, "person2type"));
                play.setPlayer2Id(rs.getString("player2_id"));
                play.setPlayer2Name(rs.getString("player2_name"));
                play.setPlayer2TeamId(rs.getString("player2_team_id"));
                play.setPlayer2TeamCity(rs.getString("player2_team_city"));
                play.setPlayer2TeamNickname(rs.getString("player2_team_nickname"));
                play.setPlayer2TeamAbbreviation(rs.getString("player2_team_abbreviation"));
                
                // Player 3
                play.setPerson3Type(getNullableDouble(rs, "person3type"));
                play.setPlayer3Id(rs.getString("player3_id"));
                play.setPlayer3Name(rs.getString("player3_name"));
                play.setPlayer3TeamId(rs.getString("player3_team_id"));
                play.setPlayer3TeamCity(rs.getString("player3_team_city"));
                play.setPlayer3TeamNickname(rs.getString("player3_team_nickname"));
                play.setPlayer3TeamAbbreviation(rs.getString("player3_team_abbreviation"));
                
                plays.add(play);
                count++;
                
                // Batch processing
                if (plays.size() >= BATCH_SIZE) {
                    playByPlayRepository.saveAll(plays);
                    logger.info("💾 Loaded {} play-by-play records...", count);
                    plays.clear();
                }
            }
            
            // Save remaining
            if (!plays.isEmpty()) {
                playByPlayRepository.saveAll(plays);
            }
        }
        
        logger.info("✅ Play-by-Play migration complete: {} records (limited for demo)", count);
        logger.info("💡 For full migration, remove the LIMIT clause and run during off-peak hours");
    }
    
    // Helper methods for null handling
    private Integer getNullableInt(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : value;
    }
    
    private Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }
}
