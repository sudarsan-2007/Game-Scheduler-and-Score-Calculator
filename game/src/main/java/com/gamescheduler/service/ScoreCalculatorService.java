package com.gamescheduler.service;

import com.gamescheduler.dao.GameDAO;
import com.gamescheduler.dao.GameDAOImpl;
import com.gamescheduler.model.Game;
import com.gamescheduler.model.GameStatus;
import com.gamescheduler.model.MatchResult;
import com.gamescheduler.util.InputValidator;

public class ScoreCalculatorService {

    private final GameDAO gameDAO;

    public ScoreCalculatorService() {
        this.gameDAO = new GameDAOImpl();
    }

    public ScoreCalculatorService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public Game recordScore(int gameId, int homeScore, int awayScore) {
        InputValidator.validatePositiveId(gameId, "Game ID");
        InputValidator.validateScore(homeScore, "Home Team");
        InputValidator.validateScore(awayScore, "Away Team");

        Game game = gameDAO.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found."));

        if (game.getStatus() == GameStatus.CANCELLED) {
            throw new IllegalStateException("Cannot enter or update scores for a cancelled game.");
        }

        boolean updated = gameDAO.updateScoreAndStatus(gameId, homeScore, awayScore, GameStatus.COMPLETED);
        if (!updated) {
            throw new IllegalStateException("Failed to update match score in database.");
        }

        game.setHomeScore(homeScore);
        game.setAwayScore(awayScore);
        game.setStatus(GameStatus.COMPLETED);
        return game;
    }

    public MatchResult calculateMatchResult(int homeScore, int awayScore) {
        InputValidator.validateScore(homeScore, "Home Team");
        InputValidator.validateScore(awayScore, "Away Team");

        if (homeScore > awayScore) {
            return MatchResult.HOME_WIN;
        } else if (awayScore > homeScore) {
            return MatchResult.AWAY_WIN;
        } else {
            return MatchResult.DRAW;
        }
    }
}
