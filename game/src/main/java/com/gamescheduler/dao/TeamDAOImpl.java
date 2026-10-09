package com.gamescheduler.dao;

import com.gamescheduler.model.Team;
import com.gamescheduler.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamDAOImpl implements TeamDAO {

    @Override
    public Team insert(Team team) {
        String sql = "INSERT INTO teams (team_name, coach_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, team.getTeamName());
            pstmt.setString(2, team.getCoachName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating team failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    team.setTeamId(generatedKeys.getInt(1));
                } else {
                    throw new RuntimeException("Creating team failed, no ID obtained.");
                }
            }

            // Fetch created_at timestamp
            return findById(team.getTeamId()).orElse(team);

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting team '" + team.getTeamName() + "': " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Team> findById(int teamId) {
        String sql = "SELECT team_id, team_name, coach_name, created_at FROM teams WHERE team_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTeam(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding team with ID " + teamId + ": " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Team> findByName(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            return Optional.empty();
        }

        String sql = "SELECT team_id, team_name, coach_name, created_at FROM teams WHERE LOWER(team_name) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teamName.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTeam(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding team by name '" + teamName + "': " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Team> findAll() {
        String sql = "SELECT team_id, team_name, coach_name, created_at FROM teams ORDER BY team_name ASC";
        List<Team> teams = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                teams.add(mapResultSetToTeam(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all teams: " + e.getMessage(), e);
        }
        return teams;
    }

    @Override
    public boolean update(Team team) {
        String sql = "UPDATE teams SET team_name = ?, coach_name = ?";
        sql += " WHERE team_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, team.getTeamName());
            pstmt.setString(2, team.getCoachName());
            pstmt.setInt(3, team.getTeamId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating team ID " + team.getTeamId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int teamId) {
        String sql = "DELETE FROM teams WHERE team_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting team ID " + teamId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasAssociatedGames(int teamId) {
        String sql = "SELECT COUNT(*) FROM games WHERE home_team_id = ? OR away_team_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);
            pstmt.setInt(2, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking game dependencies for team ID " + teamId + ": " + e.getMessage(), e);
        }
        return false;
    }

    private Team mapResultSetToTeam(ResultSet rs) throws SQLException {
        int id = rs.getInt("team_id");
        String name = rs.getString("team_name");
        String coach = rs.getString("coach_name");
        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime createdAt = (ts != null) ? ts.toLocalDateTime() : null;
        return new Team(id, name, coach, createdAt);
    }
}
