package com.gamescheduler.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Team {
    private int teamId;
    private String teamName;
    private String coachName;
    private LocalDateTime createdAt;

    public Team() {
    }

    public Team(String teamName, String coachName) {
        this.teamName = teamName;
        this.coachName = coachName;
    }

    public Team(int teamId, String teamName, String coachName, LocalDateTime createdAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.coachName = coachName;
        this.createdAt = createdAt;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return teamId == team.teamId && Objects.equals(teamName, team.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, teamName);
    }

    @Override
    public String toString() {
        return String.format("Team [ID: %d, Name: '%s', Coach: '%s', Created: %s]",
                teamId, teamName, coachName, createdAt != null ? createdAt.toString() : "N/A");
    }
}
