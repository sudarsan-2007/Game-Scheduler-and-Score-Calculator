package com.gamescheduler.dao;

import com.gamescheduler.model.Game;
import com.gamescheduler.model.GameStatus;
import com.gamescheduler.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDAOImpl implements GameDAO {

    private static final String SELECT_GAME_WITH_TEAMS_BASE =
            "SELECT g.game_id, g.home_team_id, g.away_team_id, g.game_date, g.game_time, g.venue, " +
            "       g.home_score, g.away_score, g.status, " +
            "       ht.team_name AS home_team_name, at.team_name AS away_team_name " +
            "FROM games g " +
            "LEFT JOIN teams ht ON g.home_team_id = ht.team_id " +
            "LEFT JOIN teams at ON g.away_team_id = at.team_id ";

    @Override
    public Game insert(Game game) {
        String sql = "INSERT INTO games (home_team_id, away_team_id, game_date, game_time, venue, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, game.getHomeTeamId());
            pstmt.setInt(2, game.getAwayTeamId());
            pstmt.setDate(3, game.getGameDate() != null ? java.sql.Date.valueOf(game.getGameDate()) : null);
            pstmt.setTime(4, game.getGameTime() != null ? java.sql.Time.valueOf(game.getGameTime()) : null);
            pstmt.setString(5, game.getVenue());
            pstmt.setString(6, game.getStatus() != null ? game.getStatus().name() : GameStatus.SCHEDULED.name());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating game failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    game.setGameId(generatedKeys.getInt(1));
                } else {
                    throw new RuntimeException("Creating game failed, no ID obtained.");
                }
            }

            return findById(game.getGameId()).orElse(game);

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting game: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Game> findById(int gameId) {
        String sql = SELECT_GAME_WITH_TEAMS_BASE + "WHERE g.game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGame(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding game with ID " + gameId + ": " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Game> findAll() {
        String sql = SELECT_GAME_WITH_TEAMS_BASE + "ORDER BY g.game_date ASC, g.game_time ASC";
        List<Game> games = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all games: " + e.getMessage(), e);
        }
        return games;
    }

    @Override
    public List<Game> findByTeamId(int teamId) {
        String sql = SELECT_GAME_WITH_TEAMS_BASE +
                     "WHERE g.home_team_id = ? OR g.away_team_id = ? " +
                     "ORDER BY g.game_date ASC, g.game_time ASC";
        List<Game> games = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            pstmt.setInt(2, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    games.add(mapResultSetToGame(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving games for team ID " + teamId + ": " + e.getMessage(), e);
        }
        return games;
    }

    @Override
    public List<Game> findCompletedGames() {
        String sql = SELECT_GAME_WITH_TEAMS_BASE +
                     "WHERE g.status = 'COMPLETED' " +
                     "ORDER BY g.game_date ASC, g.game_time ASC";
        List<Game> games = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                games.add(mapResultSetToGame(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving completed games: " + e.getMessage(), e);
        }
        return games;
    }

    @Override
    public boolean updateScheduledGame(Game game) {
        String sql = "UPDATE games SET home_team_id = ?, away_team_id = ?, game_date = ?, game_time = ?, venue = ? " +
                     "WHERE game_id = ? AND status = 'SCHEDULED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, game.getHomeTeamId());
            pstmt.setInt(2, game.getAwayTeamId());
            pstmt.setDate(3, game.getGameDate() != null ? java.sql.Date.valueOf(game.getGameDate()) : null);
            pstmt.setTime(4, game.getGameTime() != null ? java.sql.Time.valueOf(game.getGameTime()) : null);
            pstmt.setString(5, game.getVenue());
            pstmt.setInt(6, game.getGameId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating scheduled game ID " + game.getGameId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateScoreAndStatus(int gameId, int homeScore, int awayScore, GameStatus status) {
        String sql = "UPDATE games SET home_score = ?, away_score = ?, status = ? WHERE game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, homeScore);
            pstmt.setInt(2, awayScore);
            pstmt.setString(3, status.name());
            pstmt.setInt(4, gameId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating scores for game ID " + gameId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(int gameId, GameStatus status) {
        String sql = "UPDATE games SET status = ? WHERE game_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, gameId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating status for game ID " + gameId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Game> findOverlappingGames(int teamId, LocalDate date, LocalTime time, Integer excludeGameId) {
        StringBuilder sqlBuilder = new StringBuilder(SELECT_GAME_WITH_TEAMS_BASE)
                .append("WHERE (g.home_team_id = ? OR g.away_team_id = ?) ")
                .append("AND g.game_date = ? ")
                .append("AND g.game_time = ? ")
                .append("AND g.status <> 'CANCELLED' ");

        if (excludeGameId != null && excludeGameId > 0) {
            sqlBuilder.append("AND g.game_id <> ? ");
        }

        List<Game> games = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            pstmt.setInt(1, teamId);
            pstmt.setInt(2, teamId);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setTime(4, java.sql.Time.valueOf(time));

            if (excludeGameId != null && excludeGameId > 0) {
                pstmt.setInt(5, excludeGameId);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    games.add(mapResultSetToGame(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking overlapping games for team ID " + teamId + ": " + e.getMessage(), e);
        }
        return games;
    }

    @Override
    public boolean existsOverlappingGame(int teamId, LocalDate date, LocalTime time, Integer excludeGameId) {
        return !findOverlappingGames(teamId, date, time, excludeGameId).isEmpty();
    }

    private Game mapResultSetToGame(ResultSet rs) throws SQLException {
        int id = rs.getInt("game_id");
        int homeTeamId = rs.getInt("home_team_id");
        int awayTeamId = rs.getInt("away_team_id");
        LocalDate gameDate = rs.getDate("game_date").toLocalDate();
        LocalTime gameTime = rs.getTime("game_time").toLocalTime();
        String venue = rs.getString("venue");

        Integer homeScore = rs.getObject("home_score") != null ? rs.getInt("home_score") : null;
        Integer awayScore = rs.getObject("away_score") != null ? rs.getInt("away_score") : null;
        GameStatus status = GameStatus.fromString(rs.getString("status"));

        Game game = new Game(id, homeTeamId, awayTeamId, gameDate, gameTime, venue, homeScore, awayScore, status);
        game.setHomeTeamName(rs.getString("home_team_name"));
        game.setAwayTeamName(rs.getString("away_team_name"));
        return game;
    }
}
