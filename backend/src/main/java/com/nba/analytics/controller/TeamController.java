package com.nba.analytics.controller;

import com.nba.analytics.dto.ApiResponse;
import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.dto.stats.TeamStatsDTO;
import com.nba.analytics.model.Game;
import com.nba.analytics.model.Team;
import com.nba.analytics.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Team Controller
 * REST endpoints for team operations
 */
@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeamController {
    
    @Autowired
    private TeamService teamService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Team>>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(ApiResponse.success(teams));
    }
    
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Team>> getTeamById(@PathVariable String teamId) {
        Team team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(ApiResponse.success(team));
    }
    
    @GetMapping("/abbreviation/{abbreviation}")
    public ResponseEntity<ApiResponse<Team>> getTeamByAbbreviation(@PathVariable String abbreviation) {
        Team team = teamService.getTeamByAbbreviation(abbreviation);
        return ResponseEntity.ok(ApiResponse.success(team));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Team>>> searchTeams(@RequestParam String q) {
        List<Team> teams = teamService.searchTeams(q);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }
    
    @GetMapping("/{teamId}/games")
    public ResponseEntity<ApiResponse<PageResponse<Game>>> getTeamGames(
            @PathVariable String teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Game> games = teamService.getTeamGames(teamId, page, size);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/{teamId}/stats/{seasonId}")
    public ResponseEntity<ApiResponse<TeamStatsDTO>> getTeamStats(
            @PathVariable String teamId,
            @PathVariable String seasonId) {
        TeamStatsDTO stats = teamService.getTeamStatsBySeason(teamId, seasonId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
