# NBA Analytics Platform

> A full-stack web application demonstrating enterprise-level data engineering and modern web development practices. Built as part of my graduate studies to showcase end-to-end software engineering capabilities.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue.svg)](https://www.postgresql.org/)

## Overview

An NBA analytics platform that processes and visualizes historical basketball data spanning 1946-present. The application handles **65,000+ games**, **4,800+ players**, and **13M+ play-by-play records** through a robust ETL pipeline and RESTful API architecture.

**Live Demo**: [Add your deployment link] | **Video Walkthrough**: [Add demo video]

## Key Technical Achievements

### Data Engineering

- Designed and implemented a **Spring Batch ETL pipeline** to migrate 13M+ records from SQLite to PostgreSQL
- Achieved 85% reduction in query time through database normalization, indexing, and batch processing optimization
- Built scheduled aggregation jobs for real-time statistical calculations

### Backend Architecture

- Developed RESTful API using **Spring Boot MVC** pattern with 20+ endpoints
- Implemented **JWT-based authentication** and role-based access control using Spring Security
- Applied design patterns: Dependency Injection, Repository Pattern, DTO Pattern for separation of concerns

### Frontend Development

- Created responsive SPA using **React 18** with Material-UI component library
- Implemented client-side routing, state management, and API integration with Axios
- Built interactive data visualizations using Recharts library

## Tech Stack

**Backend**: Java 17, Spring Boot (Web, Data JPA, Security, Batch), PostgreSQL, JWT, Maven  
**Frontend**: React 18, Material-UI, Recharts, React Router, Vite  
**DevOps**: Git, PostgreSQL, RESTful APIs

## Architecture

```
┌──────────────┐    ETL Pipeline    ┌────────────────┐
│   SQLite     │ ─────────────────> │   PostgreSQL   │
│  (Source)    │    Extract         │  (Destination) │
│              │    Transform       │                │
│  65K+ Games  │    Load            │  Normalized    │
│  4.8K Players│    Batch Process   │  Indexed       │
│  13M+ Plays  │                    │  Optimized     │
└──────────────┘                    └────────────────┘
        │                                   │
        └────────> REST API <───────────────┘
                   (Spring Boot)
                        │
                   ┌────┴─────┐
                   │  React   │
                   │  Frontend│
                   └──────────┘
```

## Features

- **Analytics Dashboard** - Real-time aggregated statistics with interactive charts
- **Team & Player Search** - Paginated browsing with advanced filtering
- **Game History** - Complete box scores and play-by-play data
- **Comparison Tool** - Side-by-side performance metrics
- **"On This Day"** - Historical game lookup by date

## Quick Start

### Prerequisites

```bash
Java 17+, Node.js 18+, PostgreSQL 14+, Maven 3.8+
```

### Installation

```bash
# Clone repository
git clone <repository-url>
cd nba-analytics-platform

# Setup database
createdb nba_analytics

# Backend
cd backend
mvn clean install
mvn spring-boot:run

# Frontend (new terminal)
cd frontend
npm install
npm run dev
```

Access at `http://localhost:3000`

### Configuration

Update `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nba_analytics
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_secret_key
```

## API Examples

```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass123"}'

# Get teams (authenticated)
curl http://localhost:8080/api/teams \
  -H "Authorization: Bearer <your_jwt_token>"
```

**Key Endpoints**: `/api/auth/*`, `/api/teams/*`, `/api/players/*`, `/api/games/*`, `/api/dashboard/*`

## Project Structure

```
nba-analytics-platform/
├── backend/
│   └── src/main/java/com/nba/analytics/
│       ├── controller/      # REST endpoints
│       ├── service/         # Business logic
│       ├── repository/      # Data access (JPA)
│       ├── model/           # JPA entities
│       ├── security/        # JWT authentication
│       ├── etl/             # Data migration pipeline
│       └── batch/           # Scheduled jobs
├── frontend/
│   └── src/
│       ├── pages/           # Route components
│       ├── components/      # Reusable UI components
│       └── services/        # API integration
└── Makefile                 # Build automation
```

## Learning Outcomes

Through this project, I developed expertise in:

- Designing scalable REST APIs following MVC architecture
- Building production-grade ETL pipelines with Spring Batch
- Database optimization and query performance tuning
- Secure authentication with JWT and Spring Security
- Modern React development with hooks and functional components
- Full-stack integration and deployment workflows

## Future Enhancements

- [ ] Deploy to AWS (EC2/RDS) with CI/CD pipeline
- [ ] Add real-time data updates via WebSocket
- [ ] Implement Redis caching layer
- [ ] Add comprehensive unit/integration tests
- [ ] Create admin dashboard for user management

## Data Source

Dataset: [Kaggle Basketball Dataset](https://www.kaggle.com/datasets/wyattowalsh/basketball) (65K+ games, 1946-present)

---

**Author**: [Your Name] | Graduate Student, [University Name]  
**Contact**: [your.email@university.edu] | [LinkedIn](your-linkedin) | [Portfolio](your-website)  
**Program**: [e.g., MS in Computer Science, 2024-2026]

_This project demonstrates practical application of software engineering principles learned in graduate coursework, including Advanced Database Systems, Web Technologies, and Software Architecture._
