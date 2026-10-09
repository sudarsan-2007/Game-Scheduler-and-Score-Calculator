package com.gamescheduler.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Game {
    private int gameId;
    private int homeTeamId;
    private int awayTeamId;
    private String homeTeamName;
    private String awayTeamName;
    private LocalDate gameDate;
    private LocalTime gameTime;
    private String venue;
    private Integer homeScore;
    private Integer awayScore;
    private GameStatus status;

    public Game() {
        this.status = GameStatus.SCHEDULED;
    }

    public Game(int homeTeamId, int awayTeamId, LocalDate gameDate, LocalTime gameTime, String venue) {
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.venue = venue;
        this.status = GameStatus.SCHEDULED;
    }

    public Game(int homeTeamId, int awayTeamId, LocalDate gameDate, LocalTime gameTime, String venue, GameStatus status) {
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.venue = venue;
        this.status = status != null ? status : GameStatus.SCHEDULED;
    }

    public Game(int gameId, int homeTeamId, int awayTeamId, LocalDate gameDate, LocalTime gameTime,
                String venue, Integer homeScore, Integer awayScore, GameStatus status) {
        this.gameId = gameId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
        this.venue = venue;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public LocalTime getGameTime() {
        return gameTime;
    }

    public void setGameTime(LocalTime gameTime) {
        this.gameTime = gameTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return GameStatus.COMPLETED.equals(status);
    }

    public boolean isCancelled() {
        return GameStatus.CANCELLED.equals(status);
    }

    public boolean isScheduled() {
        return GameStatus.SCHEDULED.equals(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId == game.gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId);
    }

    @Override
    public String toString() {
        String homeStr = homeTeamName != null ? homeTeamName : "ID:" + homeTeamId;
        String awayStr = awayTeamName != null ? awayTeamName : "ID:" + awayTeamId;
        String scoreStr = isCompleted() ? String.format("%d - %d", homeScore, awayScore) : "N/A";

        return String.format("Game #%d | %s vs %s | Date: %s %s | Venue: %s | Score: %s | Status: %s",
                gameId, homeStr, awayStr, gameDate, gameTime, venue, scoreStr, status);
    }
}
