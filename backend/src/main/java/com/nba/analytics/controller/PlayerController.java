package com.nba.analytics.controller;

import com.nba.analytics.dto.ApiResponse;
import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.model.CommonPlayerInfo;
import com.nba.analytics.model.Player;
import com.nba.analytics.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Player Controller
 * REST endpoints for player operations
 */
@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlayerController {
    
    @Autowired
    private PlayerService playerService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Player>>> getAllPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Player> players = playerService.getAllPlayers(page, size);
        return ResponseEntity.ok(ApiResponse.success(players));
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<PageResponse<Player>>> getActivePlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Player> players = playerService.getActivePlayers(page, size);
        return ResponseEntity.ok(ApiResponse.success(players));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<Player>>> searchPlayers(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Player> players = playerService.searchPlayers(q, page, size);
        return ResponseEntity.ok(ApiResponse.success(players));
    }
    
    @GetMapping("/{playerId}")
    public ResponseEntity<ApiResponse<Player>> getPlayerById(@PathVariable String playerId) {
        Player player = playerService.getPlayerById(playerId);
        return ResponseEntity.ok(ApiResponse.success(player));
    }
    
    @GetMapping("/{playerId}/details")
    public ResponseEntity<ApiResponse<CommonPlayerInfo>> getPlayerDetails(@PathVariable String playerId) {
        CommonPlayerInfo details = playerService.getPlayerDetailedInfo(playerId);
        return ResponseEntity.ok(ApiResponse.success(details));
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<ApiResponse<List<CommonPlayerInfo>>> getPlayersByTeam(@PathVariable Integer teamId) {
        List<CommonPlayerInfo> players = playerService.getPlayersByTeam(teamId);
        return ResponseEntity.ok(ApiResponse.success(players));
    }
    
    @GetMapping("/positions")
    public ResponseEntity<ApiResponse<List<String>>> getAllPositions() {
        List<String> positions = playerService.getAllPositions();
        return ResponseEntity.ok(ApiResponse.success(positions));
    }
    
    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<String>>> getAllCountries() {
        List<String> countries = playerService.getAllCountries();
        return ResponseEntity.ok(ApiResponse.success(countries));
    }
}
