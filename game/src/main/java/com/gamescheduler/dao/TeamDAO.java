package com.gamescheduler.dao;

import com.gamescheduler.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDAO {
    Team insert(Team team);
    Optional<Team> findById(int teamId);
    Optional<Team> findByName(String teamName);
    List<Team> findAll();
    boolean update(Team team);
    boolean delete(int teamId);
    boolean hasAssociatedGames(int teamId);
}
