package com.gamescheduler.service;

import com.gamescheduler.dao.GameDAO;
import com.gamescheduler.dao.GameDAOImpl;
import com.gamescheduler.dao.TeamDAO;
import com.gamescheduler.dao.TeamDAOImpl;
import com.gamescheduler.model.Game;
import com.gamescheduler.model.GameStatus;
import com.gamescheduler.util.InputValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class GameService {

    private final GameDAO gameDAO;
    private final TeamDAO teamDAO;

    public GameService() {
        this.gameDAO = new GameDAOImpl();
        this.teamDAO = new TeamDAOImpl();
    }

    public GameService(GameDAO gameDAO, TeamDAO teamDAO) {
        this.gameDAO = gameDAO;
        this.teamDAO = teamDAO;
    }

    public Game scheduleGame(int homeTeamId, int awayTeamId, LocalDate date, LocalTime time, String venue) {
        InputValidator.validateTeamsNotSame(homeTeamId, awayTeamId);
        InputValidator.validateVenue(venue);

        if (teamDAO.findById(homeTeamId).isEmpty()) {
            throw new IllegalArgumentException("Home Team ID " + homeTeamId + " does not exist.");
        }
        if (teamDAO.findById(awayTeamId).isEmpty()) {
            throw new IllegalArgumentException("Away Team ID " + awayTeamId + " does not exist.");
        }

        if (gameDAO.existsOverlappingGame(homeTeamId, date, time, 0)) {
            throw new IllegalStateException("Home team already has an overlapping game scheduled on " + date + " at " + time);
        }
        if (gameDAO.existsOverlappingGame(awayTeamId, date, time, 0)) {
            throw new IllegalStateException("Away team already has an overlapping game scheduled on " + date + " at " + time);
        }

        Game game = new Game(homeTeamId, awayTeamId, date, time, venue);
        return gameDAO.insert(game);
    }

    public List<Game> getAllGames() {
        return gameDAO.findAll();
    }

    public Game getGameById(int gameId) {
        InputValidator.validatePositiveId(gameId, "Game ID");
        return gameDAO.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found."));
    }

    public List<Game> getGamesByTeam(int teamId) {
        InputValidator.validatePositiveId(teamId, "Team ID");
        if (teamDAO.findById(teamId).isEmpty()) {
            throw new IllegalArgumentException("Team with ID " + teamId + " not found.");
        }
        return gameDAO.findByTeamId(teamId);
    }

    public boolean updateScheduledGame(int gameId, int homeTeamId, int awayTeamId, LocalDate date, LocalTime time, String venue) {
        Game existing = getGameById(gameId);

        if (existing.getStatus() == GameStatus.COMPLETED) {
            throw new IllegalStateException("Cannot modify schedule of a completed game.");
        }

        InputValidator.validateTeamsNotSame(homeTeamId, awayTeamId);
        InputValidator.validateVenue(venue);

        if (teamDAO.findById(homeTeamId).isEmpty()) {
            throw new IllegalArgumentException("Home Team ID " + homeTeamId + " does not exist.");
        }
        if (teamDAO.findById(awayTeamId).isEmpty()) {
            throw new IllegalArgumentException("Away Team ID " + awayTeamId + " does not exist.");
        }

        if (gameDAO.existsOverlappingGame(homeTeamId, date, time, gameId)) {
            throw new IllegalStateException("Home team has a conflicting game scheduled on " + date + " at " + time);
        }
        if (gameDAO.existsOverlappingGame(awayTeamId, date, time, gameId)) {
            throw new IllegalStateException("Away team has a conflicting game scheduled on " + date + " at " + time);
        }

        existing.setHomeTeamId(homeTeamId);
        existing.setAwayTeamId(awayTeamId);
        existing.setGameDate(date);
        existing.setGameTime(time);
        existing.setVenue(venue);

        return gameDAO.updateScheduledGame(existing);
    }

    public boolean cancelGame(int gameId) {
        Game existing = getGameById(gameId);
        if (existing.getStatus() == GameStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a game that is already completed.");
        }
        return gameDAO.updateStatus(gameId, GameStatus.CANCELLED);
    }
}
