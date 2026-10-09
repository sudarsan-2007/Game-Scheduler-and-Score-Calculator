package com.gamescheduler.dao;

import com.gamescheduler.model.Game;
import com.gamescheduler.model.GameStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface GameDAO {
    Game insert(Game game);
    Optional<Game> findById(int gameId);
    List<Game> findAll();
    List<Game> findByTeamId(int teamId);
    List<Game> findCompletedGames();
    boolean updateScheduledGame(Game game);
    boolean updateScoreAndStatus(int gameId, int homeScore, int awayScore, GameStatus status);
    boolean updateStatus(int gameId, GameStatus status);
    List<Game> findOverlappingGames(int teamId, LocalDate date, LocalTime time, Integer excludeGameId);
    boolean existsOverlappingGame(int teamId, LocalDate date, LocalTime time, Integer excludeGameId);
}
