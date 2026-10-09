package com.gamescheduler.service;

import com.gamescheduler.dao.TeamDAO;
import com.gamescheduler.dao.TeamDAOImpl;
import com.gamescheduler.model.Team;
import com.gamescheduler.util.InputValidator;

import java.util.List;
import java.util.Optional;

public class TeamService {

    private final TeamDAO teamDAO;

    public TeamService() {
        this.teamDAO = new TeamDAOImpl();
    }

    public TeamService(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    public Team addTeam(String teamName, String coachName) {
        InputValidator.validateTeamName(teamName);
        InputValidator.validateCoachName(coachName);

        if (teamDAO.findByName(teamName).isPresent()) {
            throw new IllegalArgumentException("Team with name '" + teamName + "' already exists.");
        }

        return teamDAO.insert(new Team(teamName, coachName));
    }

    public List<Team> getAllTeams() {
        return teamDAO.findAll();
    }

    public Team getTeamById(int teamId) {
        InputValidator.validatePositiveId(teamId, "Team ID");
        return teamDAO.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found."));
    }

    public Team getTeamByName(String teamName) {
        InputValidator.validateTeamName(teamName);
        return teamDAO.findByName(teamName)
                .orElseThrow(() -> new IllegalArgumentException("Team with name '" + teamName + "' not found."));
    }

    public boolean updateTeam(int teamId, String newName, String newCoach) {
        InputValidator.validatePositiveId(teamId, "Team ID");
        InputValidator.validateTeamName(newName);
        InputValidator.validateCoachName(newCoach);

        Team existing = getTeamById(teamId);

        Optional<Team> existingName = teamDAO.findByName(newName);
        if (existingName.isPresent() && existingName.get().getTeamId() != teamId) {
            throw new IllegalArgumentException("Team name '" + newName + "' is already taken by another team.");
        }

        existing.setTeamName(newName);
        existing.setCoachName(newCoach);

        return teamDAO.update(existing);
    }

    public boolean deleteTeam(int teamId) {
        InputValidator.validatePositiveId(teamId, "Team ID");
        Team existing = getTeamById(teamId);

        if (teamDAO.hasAssociatedGames(teamId)) {
            throw new IllegalStateException("Cannot delete team '" + existing.getTeamName() + "' because it has associated scheduled or completed games.");
        }

        return teamDAO.delete(teamId);
    }
}
