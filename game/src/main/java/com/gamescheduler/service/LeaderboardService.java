package com.gamescheduler.service;

import com.gamescheduler.dao.GameDAO;
import com.gamescheduler.dao.GameDAOImpl;
import com.gamescheduler.dao.TeamDAO;
import com.gamescheduler.dao.TeamDAOImpl;
import com.gamescheduler.model.Game;
import com.gamescheduler.model.GameStatus;
import com.gamescheduler.model.Team;
import com.gamescheduler.model.TeamStanding;

import java.util.*;

public class LeaderboardService {

    private final TeamDAO teamDAO;
    private final GameDAO gameDAO;

    public LeaderboardService() {
        this.teamDAO = new TeamDAOImpl();
        this.gameDAO = new GameDAOImpl();
    }

    public LeaderboardService(TeamDAO teamDAO, GameDAO gameDAO) {
        this.teamDAO = teamDAO;
        this.gameDAO = gameDAO;
    }

    public List<TeamStanding> calculateLeaderboard() {
        List<Team> teams = teamDAO.findAll();
        List<Game> games = gameDAO.findAll();

        Map<Integer, TeamStanding> standingsMap = new HashMap<>();
        for (Team t : teams) {
            standingsMap.put(t.getTeamId(), new TeamStanding(t.getTeamId(), t.getTeamName()));
        }

        for (Game g : games) {
            if (g.getStatus() == GameStatus.COMPLETED && g.getHomeScore() != null && g.getAwayScore() != null) {
                TeamStanding home = standingsMap.get(g.getHomeTeamId());
                TeamStanding away = standingsMap.get(g.getAwayTeamId());

                if (home != null && away != null) {
                    int homeScore = g.getHomeScore();
                    int awayScore = g.getAwayScore();

                    if (homeScore > awayScore) {
                        home.addWin(homeScore, awayScore);
                        away.addLoss(awayScore, homeScore);
                    } else if (awayScore > homeScore) {
                        away.addWin(awayScore, homeScore);
                        home.addLoss(homeScore, awayScore);
                    } else {
                        home.addDraw(homeScore, awayScore);
                        away.addDraw(awayScore, homeScore);
                    }
                }
            }
        }

        List<TeamStanding> standings = new ArrayList<>(standingsMap.values());
        Collections.sort(standings);

        int rank = 1;
        for (TeamStanding standing : standings) {
            standing.setRank(rank++);
        }

        return standings;
    }
}
