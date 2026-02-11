package com.nba.analytics.service;

import com.nba.analytics.dto.PageResponse;
import com.nba.analytics.exception.ResourceNotFoundException;
import com.nba.analytics.model.CommonPlayerInfo;
import com.nba.analytics.model.Player;
import com.nba.analytics.repository.CommonPlayerInfoRepository;
import com.nba.analytics.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Player Service
 * Business logic for player operations
 */
@Service
@Transactional(readOnly = true)
public class PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;
    
    @Autowired
    private CommonPlayerInfoRepository commonPlayerInfoRepository;
    
    public Player getPlayerById(String playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + playerId));
    }
    
    public CommonPlayerInfo getPlayerDetailedInfo(String playerId) {
        return commonPlayerInfoRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("Player detailed info not found for id: " + playerId));
    }
    
    public PageResponse<Player> getAllPlayers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fullName"));
        Page<Player> playerPage = playerRepository.findAll(pageable);
        
        return buildPageResponse(playerPage);
    }
    
    public PageResponse<Player> getActivePlayers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "fullName"));
        Page<Player> playerPage = playerRepository.findByIsActive(1, pageable);
        
        return buildPageResponse(playerPage);
    }
    
    public PageResponse<Player> searchPlayers(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Player> playerPage = playerRepository.searchPlayers(searchTerm, pageable);
        
        return buildPageResponse(playerPage);
    }
    
    public List<CommonPlayerInfo> getPlayersByTeam(Integer teamId) {
        return commonPlayerInfoRepository.findByTeamId(teamId);
    }
    
    public List<CommonPlayerInfo> getActivePlayersByTeam(Integer teamId) {
        return commonPlayerInfoRepository.findActivePlayersByTeam(teamId);
    }
    
    public List<String> getAllPositions() {
        return commonPlayerInfoRepository.findAllPositions();
    }
    
    public List<String> getAllCountries() {
        return commonPlayerInfoRepository.findAllCountries();
    }
    
    public long getTotalPlayersCount() {
        return playerRepository.count();
    }
    
    public long getActivePlayersCount() {
        return playerRepository.countActivePlayers();
    }
    
    private PageResponse<Player> buildPageResponse(Page<Player> playerPage) {
        return PageResponse.<Player>builder()
                .content(playerPage.getContent())
                .pageNumber(playerPage.getNumber())
                .pageSize(playerPage.getSize())
                .totalElements(playerPage.getTotalElements())
                .totalPages(playerPage.getTotalPages())
                .last(playerPage.isLast())
                .first(playerPage.isFirst())
                .build();
    }
}
