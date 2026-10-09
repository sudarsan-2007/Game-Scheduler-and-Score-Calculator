package com.gamescheduler.model;

import java.util.Comparator;
import java.util.Objects;

public class TeamStanding implements Comparable<TeamStanding> {
    private int rank;
    private int teamId;
    private String teamName;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int pointsScored;
    private int pointsConceded;
    private int scoreDifference;
    private int leaguePoints;

    public TeamStanding() {
    }

    public TeamStanding(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public TeamStanding(int rank, int teamId, String teamName, int matchesPlayed,
                        int wins, int draws, int losses, int pointsScored,
                        int pointsConceded, int scoreDifference, int leaguePoints) {
        this.rank = rank;
        this.teamId = teamId;
        this.teamName = teamName;
        this.matchesPlayed = matchesPlayed;
        this.wins = wins;
        this.draws = draws;
        this.losses = losses;
        this.pointsScored = pointsScored;
        this.pointsConceded = pointsConceded;
        this.scoreDifference = scoreDifference;
        this.leaguePoints = leaguePoints;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getPointsScored() {
        return pointsScored;
    }

    public void setPointsScored(int pointsScored) {
        this.pointsScored = pointsScored;
    }

    public int getPointsConceded() {
        return pointsConceded;
    }

    public void setPointsConceded(int pointsConceded) {
        this.pointsConceded = pointsConceded;
    }

    public int getScoreDifference() {
        return scoreDifference;
    }

    public void setScoreDifference(int scoreDifference) {
        this.scoreDifference = scoreDifference;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public void addWin(int scored, int conceded) {
        this.matchesPlayed++;
        this.wins++;
        this.pointsScored += scored;
        this.pointsConceded += conceded;
        recalculateMetrics();
    }

    public void addDraw(int scored, int conceded) {
        this.matchesPlayed++;
        this.draws++;
        this.pointsScored += scored;
        this.pointsConceded += conceded;
        recalculateMetrics();
    }

    public void addLoss(int scored, int conceded) {
        this.matchesPlayed++;
        this.losses++;
        this.pointsScored += scored;
        this.pointsConceded += conceded;
        recalculateMetrics();
    }

    public void recalculateMetrics() {
        this.scoreDifference = this.pointsScored - this.pointsConceded;
        this.leaguePoints = (this.wins * 3) + (this.draws * 1);
    }

    /**
     * Comparator for Leaderboard Sorting:
     * 1. League points, descending.
     * 2. Score difference, descending.
     * 3. Points scored, descending.
     * 4. Team name, ascending.
     */
    public static final Comparator<TeamStanding> LEADERBOARD_COMPARATOR = (s1, s2) -> {
        int cmpPoints = Integer.compare(s2.getLeaguePoints(), s1.getLeaguePoints());
        if (cmpPoints != 0) return cmpPoints;

        int cmpDiff = Integer.compare(s2.getScoreDifference(), s1.getScoreDifference());
        if (cmpDiff != 0) return cmpDiff;

        int cmpScored = Integer.compare(s2.getPointsScored(), s1.getPointsScored());
        if (cmpScored != 0) return cmpScored;

        return s1.getTeamName().compareToIgnoreCase(s2.getTeamName());
    };

    @Override
    public int compareTo(TeamStanding other) {
        return LEADERBOARD_COMPARATOR.compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamStanding that = (TeamStanding) o;
        return teamId == that.teamId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId);
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %2d | %2d | %2d | %2d | %3d | %3d | %4d | %3d",
                rank, teamName, matchesPlayed, wins, draws, losses, pointsScored, pointsConceded, scoreDifference, leaguePoints);
    }
}
