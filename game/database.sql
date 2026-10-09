-- Database Initialization Script for Game Scheduler and Score Calculator
-- MySQL 8.0+

CREATE DATABASE IF NOT EXISTS game_scheduler_db;
USE game_scheduler_db;

-- Drop existing tables if re-initialising
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS teams;

-- -----------------------------------------------------
-- Table: teams
-- -----------------------------------------------------
CREATE TABLE teams (
    team_id INT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL UNIQUE,
    coach_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Table: games
-- -----------------------------------------------------
CREATE TABLE games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    home_team_id INT NOT NULL,
    away_team_id INT NOT NULL,
    game_date DATE NOT NULL,
    game_time TIME NOT NULL,
    venue VARCHAR(150) NOT NULL,
    home_score INT DEFAULT NULL,
    away_score INT DEFAULT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT fk_home_team FOREIGN KEY (home_team_id) REFERENCES teams(team_id) ON DELETE RESTRICT,
    CONSTRAINT fk_away_team FOREIGN KEY (away_team_id) REFERENCES teams(team_id) ON DELETE RESTRICT,
    CONSTRAINT chk_different_teams CHECK (home_team_id <> away_team_id),
    CONSTRAINT chk_home_score_non_negative CHECK (home_score IS NULL OR home_score >= 0),
    CONSTRAINT chk_away_score_non_negative CHECK (away_score IS NULL OR away_score >= 0),
    CONSTRAINT chk_valid_status CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Indexes for optimize query performance
CREATE INDEX idx_game_date_time ON games (game_date, game_time);
CREATE INDEX idx_home_team ON games (home_team_id);
CREATE INDEX idx_away_team ON games (away_team_id);
CREATE INDEX idx_game_status ON games (status);

-- -----------------------------------------------------
-- Sample Initialization Data
-- -----------------------------------------------------
INSERT INTO teams (team_name, coach_name) VALUES 
('Thunder Strikers', 'Alex Ferguson'),
('Cyber Dragons', 'Jose Mourinho'),
('Solar Flare FC', 'Pep Guardiola'),
('Vortex United', 'Jurgen Klopp');

-- Sample Games
-- Game 1: Completed match
INSERT INTO games (home_team_id, away_team_id, game_date, game_time, venue, home_score, away_score, status) 
VALUES (1, 2, '2026-10-01', '15:00:00', 'Central Arena', 3, 1, 'COMPLETED');

-- Game 2: Completed match
INSERT INTO games (home_team_id, away_team_id, game_date, game_time, venue, home_score, away_score, status) 
VALUES (3, 4, '2026-10-02', '18:00:00', 'Metropolitan Stadium', 2, 2, 'COMPLETED');

-- Game 3: Scheduled future match
INSERT INTO games (home_team_id, away_team_id, game_date, game_time, venue, status) 
VALUES (1, 3, '2026-10-15', '16:00:00', 'Central Arena', 'SCHEDULED');

-- Game 4: Scheduled future match
INSERT INTO games (home_team_id, away_team_id, game_date, game_time, venue, status) 
VALUES (2, 4, '2026-10-16', '19:30:00', 'Cyber Dome', 'SCHEDULED');
