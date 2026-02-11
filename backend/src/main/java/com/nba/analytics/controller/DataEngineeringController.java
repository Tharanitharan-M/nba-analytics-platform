package com.nba.analytics.controller;

import com.nba.analytics.dto.ApiResponse;
import com.nba.analytics.etl.DataMigrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Data Engineering Controller
 * Manages ETL pipelines and data processing jobs
 * Restricted to admin users only
 */
@RestController
@RequestMapping("/api/data-engineering")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataEngineeringController {
    
    private static final Logger logger = LoggerFactory.getLogger(DataEngineeringController.class);
    
    @Autowired
    private DataMigrationService migrationService;
    
    /**
     * Trigger data migration from SQLite to PostgreSQL
     * Admin only endpoint
     */
    @PostMapping("/migrate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> migrateData(@RequestParam String sqlitePath) {
        try {
            migrationService.migrateAllData(sqlitePath);
            return ResponseEntity.ok(ApiResponse.success("Data migration completed successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("Migration failed: " + e.getMessage()));
        }
    }
    
    /**
     * Simple migration endpoint for initial setup (no auth required)
     * Use this for first-time data load
     */
    @PostMapping("/migrate-initial")
    public ResponseEntity<ApiResponse<String>> migrateInitialData() {
        try {
            String sqlitePath = "/Users/tharani/new_project/nba.sqlite";
            logger.info("Starting initial data migration from: {}", sqlitePath);
            migrationService.migrateAllData(sqlitePath);
            return ResponseEntity.ok(ApiResponse.success("Initial data migration completed successfully"));
        } catch (Exception e) {
            logger.error("Migration failed", e);
            return ResponseEntity.ok(ApiResponse.error("Migration failed: " + e.getMessage()));
        }
    }
    
    /**
     * Public endpoint to check migration status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getMigrationStatus() {
        return ResponseEntity.ok(ApiResponse.success("Migration service is ready"));
    }
}
