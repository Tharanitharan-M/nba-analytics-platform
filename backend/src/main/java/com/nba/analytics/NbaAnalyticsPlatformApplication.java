package com.nba.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application Class for NBA Analytics Platform
 * 
 * This application provides comprehensive NBA analytics with:
 * - RESTful APIs for teams, players, games data
 * - JWT-based authentication and authorization
 * - Data engineering pipelines for ETL and aggregation
 * - Advanced analytics and statistics calculation
 * 
 * @author NBA Analytics Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class NbaAnalyticsPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbaAnalyticsPlatformApplication.class, args);
        System.out.println("\n🏀 NBA Analytics Platform Started Successfully! 🏀\n");
    }
}
