package com.nba.analytics.controller;

import com.nba.analytics.dto.ApiResponse;
import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.model.Game;
import com.nba.analytics.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Game Controller
 * REST endpoints for game operations
 */
@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Game>>> getRecentGames(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Game> games = gameService.getRecentGames(page, size);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/{gameId}")
    public ResponseEntity<ApiResponse<Game>> getGameById(@PathVariable String gameId) {
        Game game = gameService.getGameById(gameId);
        return ResponseEntity.ok(ApiResponse.success(game));
    }
    
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<ApiResponse<PageResponse<Game>>> getGamesBySeason(
            @PathVariable String seasonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Game> games = gameService.getGamesBySeason(seasonId, page, size);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<ApiResponse<PageResponse<Game>>> getGamesByTeam(
            @PathVariable String teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Game> games = gameService.getGamesByTeam(teamId, page, size);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<PageResponse<Game>>> getGamesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageResponse<Game> games = gameService.getGamesByDateRange(startDate, endDate, page, size);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/on-this-day")
    public ResponseEntity<ApiResponse<List<Game>>> getGamesOnThisDay(
            @RequestParam int month,
            @RequestParam int day) {
        List<Game> games = gameService.getGamesOnThisDay(month, day);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
    
    @GetMapping("/seasons")
    public ResponseEntity<ApiResponse<List<String>>> getAllSeasons() {
        List<String> seasons = gameService.getAllSeasons();
        return ResponseEntity.ok(ApiResponse.success(seasons));
    }
    
    @GetMapping("/high-scoring")
    public ResponseEntity<ApiResponse<List<Game>>> getHighScoringGames(
            @RequestParam(defaultValue = "10") int limit) {
        List<Game> games = gameService.getHighScoringGames(limit);
        return ResponseEntity.ok(ApiResponse.success(games));
    }
}
