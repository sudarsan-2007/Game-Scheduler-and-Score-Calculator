package com.gamescheduler;

import com.gamescheduler.model.*;
import com.gamescheduler.service.*;
import com.gamescheduler.util.InputValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final TeamService teamService;
    private final GameService gameService;
    private final ScoreCalculatorService scoreCalculatorService;
    private final LeaderboardService leaderboardService;
    private final Scanner scanner;

    public Main() {
        this.teamService = new TeamService();
        this.gameService = new GameService();
        this.scoreCalculatorService = new ScoreCalculatorService();
        this.leaderboardService = new LeaderboardService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("=========================================================================");
        System.out.println("            GAME SCHEDULER & SCORE CALCULATOR SYSTEM             ");
        System.out.println("=========================================================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readIntInput("Select an option: ");
            System.out.println();
            try {
                switch (choice) {
                    case 1 -> handleAddTeam();
                    case 2 -> handleViewAllTeams();
                    case 3 -> handleSearchTeam();
                    case 4 -> handleUpdateTeam();
                    case 5 -> handleDeleteTeam();
                    case 6 -> handleScheduleGame();
                    case 7 -> handleViewGameSchedule();
                    case 8 -> handleSearchGame();
                    case 9 -> handleUpdateScheduledGame();
                    case 10 -> handleCancelGame();
                    case 11 -> handleEnterOrUpdateScore();
                    case 12 -> handleViewMatchResult();
                    case 13 -> handleViewLeaderboard();
                    case 0 -> {
                        System.out.println("Exiting Game Scheduler System. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid option! Please select a choice between 0 and 13.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("SYSTEM ERROR: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(" 1. Add team                 |  8. Search game");
        System.out.println(" 2. View all teams           |  9. Update scheduled game");
        System.out.println(" 3. Search team              | 10. Cancel game");
        System.out.println(" 4. Update team              | 11. Enter or update score");
        System.out.println(" 5. Delete team              | 12. View match result");
        System.out.println(" 6. Schedule game            | 13. View leaderboard");
        System.out.println(" 7. View game schedule       |  0. Exit");
        System.out.println("-------------------------------------------------------------------------");
    }

    // --- Module 1: Team Management handlers ---

    private void handleAddTeam() {
        System.out.println("=== Add New Team ===");
        String name = readStringInput("Enter Team Name: ");
        String coach = readStringInput("Enter Coach Name: ");

        Team team = teamService.addTeam(name, coach);
        System.out.println("SUCCESS: Team added successfully! " + team);
    }

    private void handleViewAllTeams() {
        System.out.println("=== All Registered Teams ===");
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("No teams registered in the system yet.");
            return;
        }

        System.out.printf("%-6s | %-25s | %-20s | %-20s%n", "ID", "Team Name", "Coach", "Created At");
        System.out.println("-------------------------------------------------------------------------");
        for (Team t : teams) {
            System.out.printf("%-6d | %-25s | %-20s | %-20s%n",
                    t.getTeamId(), t.getTeamName(), t.getCoachName(),
                    t.getCreatedAt() != null ? t.getCreatedAt().toString() : "N/A");
        }
    }

    private void handleSearchTeam() {
        System.out.println("=== Search Team ===");
        System.out.println("Search by: 1. Team ID  2. Team Name");
        int option = readIntInput("Enter choice (1 or 2): ");

        if (option == 1) {
            int teamId = readIntInput("Enter Team ID: ");
            Team team = teamService.getTeamById(teamId);
            System.out.println("FOUND: " + team);
        } else if (option == 2) {
            String name = readStringInput("Enter Team Name: ");
            Team team = teamService.getTeamByName(name);
            System.out.println("FOUND: " + team);
        } else {
            System.out.println("Invalid search choice.");
        }
    }

    private void handleUpdateTeam() {
        System.out.println("=== Update Team ===");
        int teamId = readIntInput("Enter Team ID to update: ");
        Team current = teamService.getTeamById(teamId);
        System.out.println("Current Details: " + current);

        String newName = readStringInput("Enter New Team Name: ");
        String newCoach = readStringInput("Enter New Coach Name: ");

        boolean updated = teamService.updateTeam(teamId, newName, newCoach);
        if (updated) {
            System.out.println("SUCCESS: Team details updated successfully.");
        } else {
            System.out.println("Failed to update team.");
        }
    }

    private void handleDeleteTeam() {
        System.out.println("=== Delete Team ===");
        int teamId = readIntInput("Enter Team ID to delete: ");
        Team team = teamService.getTeamById(teamId);

        String confirm = readStringInput(
                "Are you sure you want to delete team '" + team.getTeamName() + "'? (y/n): ");
        if ("y".equalsIgnoreCase(confirm.trim())) {
            boolean deleted = teamService.deleteTeam(teamId);
            if (deleted) {
                System.out.println("SUCCESS: Team deleted successfully.");
            } else {
                System.out.println("Failed to delete team.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // --- Module 2: Game Scheduling handlers ---

    private void handleScheduleGame() {
        System.out.println("=== Schedule New Game ===");
        handleViewAllTeams();
        System.out.println();

        int homeId = readIntInput("Enter Home Team ID: ");
        int awayId = readIntInput("Enter Away Team ID: ");
        String dateStr = readStringInput("Enter Game Date (YYYY-MM-DD): ");
        String timeStr = readStringInput("Enter Game Time (HH:MM e.g. 15:30): ");
        String venue = readStringInput("Enter Venue Name: ");

        LocalDate date = InputValidator.parseDate(dateStr);
        LocalTime time = InputValidator.parseTime(timeStr);

        Game scheduled = gameService.scheduleGame(homeId, awayId, date, time, venue);
        System.out.println("SUCCESS: Game scheduled successfully!");
        System.out.println(scheduled);
    }

    private void handleViewGameSchedule() {
        System.out.println("=== Scheduled & Completed Games ===");
        List<Game> games = gameService.getAllGames();
        if (games.isEmpty()) {
            System.out.println("No games scheduled in the system.");
            return;
        }
        printGameTable(games);
    }

    private void handleSearchGame() {
        System.out.println("=== Search Game ===");
        System.out.println("Search by: 1. Game ID  2. Team ID");
        int option = readIntInput("Enter choice (1 or 2): ");

        if (option == 1) {
            int gameId = readIntInput("Enter Game ID: ");
            Game game = gameService.getGameById(gameId);
            System.out.println("FOUND: " + game);
        } else if (option == 2) {
            int teamId = readIntInput("Enter Team ID: ");
            List<Game> games = gameService.getGamesByTeam(teamId);
            if (games.isEmpty()) {
                System.out.println("No games found for Team ID " + teamId + ".");
            } else {
                printGameTable(games);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void handleUpdateScheduledGame() {
        System.out.println("=== Update Scheduled Game ===");
        int gameId = readIntInput("Enter Game ID to update: ");
        Game existing = gameService.getGameById(gameId);
        System.out.println("Current Game: " + existing);

        int homeId = readIntInput("Enter New Home Team ID: ");
        int awayId = readIntInput("Enter New Away Team ID: ");
        String dateStr = readStringInput("Enter New Game Date (YYYY-MM-DD): ");
        String timeStr = readStringInput("Enter New Game Time (HH:MM): ");
        String venue = readStringInput("Enter New Venue: ");

        LocalDate date = InputValidator.parseDate(dateStr);
        LocalTime time = InputValidator.parseTime(timeStr);

        boolean updated = gameService.updateScheduledGame(gameId, homeId, awayId, date, time, venue);
        if (updated) {
            System.out.println("SUCCESS: Scheduled game updated successfully.");
        } else {
            System.out.println("Failed to update scheduled game.");
        }
    }

    private void handleCancelGame() {
        System.out.println("=== Cancel Game ===");
        int gameId = readIntInput("Enter Game ID to cancel: ");
        Game game = gameService.getGameById(gameId);

        String confirm = readStringInput("Are you sure you want to cancel Game #" + gameId + "? (y/n): ");
        if ("y".equalsIgnoreCase(confirm.trim())) {
            boolean cancelled = gameService.cancelGame(gameId);
            if (cancelled) {
                System.out.println("SUCCESS: Game #" + gameId + " cancelled.");
            } else {
                System.out.println("Failed to cancel game.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    // --- Module 3 & 4: Score Management & Calculator handlers ---

    private void handleEnterOrUpdateScore() {
        System.out.println("=== Enter / Update Match Score ===");
        int gameId = readIntInput("Enter Game ID: ");
        int homeScore = readIntInput("Enter Home Team Score: ");
        int awayScore = readIntInput("Enter Away Team Score: ");

        Game game = scoreCalculatorService.recordScore(gameId, homeScore, awayScore);
        MatchResult result = scoreCalculatorService.calculateMatchResult(homeScore, awayScore);

        String resultStr = switch (result) {
            case HOME_WIN -> game.getHomeTeamName() + " Wins";
            case AWAY_WIN -> game.getAwayTeamName() + " Wins";
            case DRAW -> "Draw";
        };

        System.out.println("\nSUCCESS: Score recorded successfully!");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Match Result Summary : " + game.getHomeTeamName() + " " + homeScore + " - " + awayScore
                + " " + game.getAwayTeamName());
        System.out.println("Result               : " + resultStr);
        System.out.println("-------------------------------------------------------------------------");
    }

    private void handleViewMatchResult() {
        System.out.println("=== View Match Result ===");
        int gameId = readIntInput("Enter Game ID: ");
        Game g = gameService.getGameById(gameId);

        if (g.getStatus() != GameStatus.COMPLETED) {
            System.out.println("Game ID #" + gameId + " is not completed yet. Current status: " + g.getStatus());
            return;
        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Game ID              : " + g.getGameId());
        System.out.println("Home Team            : " + g.getHomeTeamName() + " (" + g.getHomeScore() + ")");
        System.out.println("Away Team            : " + g.getAwayTeamName() + " (" + g.getAwayScore() + ")");
        System.out.println("Score                : " + g.getHomeScore() + " - " + g.getAwayScore());
        System.out.println("-------------------------------------------------------------------------");
    }

    // --- Module 5: Leaderboard handler ---

    private void handleViewLeaderboard() {
        System.out.println("==========================================================================================");
        System.out.println("                               LEAGUE STANDINGS LEADERBOARD                               ");
        System.out.println("==========================================================================================");

        List<TeamStanding> standings = leaderboardService.calculateLeaderboard();

        if (standings.isEmpty()) {
            System.out.println("No teams found in league.");
            return;
        }

        System.out.printf("%-4s | %-22s | %2s | %2s | %2s | %2s | %3s | %3s | %4s | %3s%n",
                "Rank", "Team Name", "MP", "W", "D", "L", "GF", "GA", "GD", "PTS");
        System.out.println("------------------------------------------------------------------------------------------");
        for (TeamStanding ts : standings) {
            System.out.println(ts.toString());
        }
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(
                "Legend: MP=Matches Played, W=Wins, D=Draws, L=Losses, GF=Goals For, GA=Goals Against, GD=Goal Diff, PTS=Points");
    }

    // --- Helper Input Routines ---

    private void printGameTable(List<Game> games) {
        System.out.printf("%-6s | %-20s | %-20s | %-12s | %-8s | %-15s | %-7s | %-10s%n",
                "ID", "Home Team", "Away Team", "Date", "Time", "Venue", "Score", "Status");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for (Game g : games) {
            String homeName = g.getHomeTeamName() != null ? g.getHomeTeamName() : "ID:" + g.getHomeTeamId();
            String awayName = g.getAwayTeamName() != null ? g.getAwayTeamName() : "ID:" + g.getAwayTeamId();
            String scoreStr = g.isCompleted() ? (g.getHomeScore() + " - " + g.getAwayScore()) : "N/A";

            System.out.printf("%-6d | %-20s | %-20s | %-12s | %-8s | %-15s | %-7s | %-10s%n",
                    g.getGameId(), homeName, awayName, g.getGameDate(), g.getGameTime(), g.getVenue(), scoreStr,
                    g.getStatus());
        }
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer format! Please enter a valid number.");
            }
        }
    }
}