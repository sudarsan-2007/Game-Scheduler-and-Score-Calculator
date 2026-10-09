package com.gamescheduler.exception;

/**
 * Thrown when a team cannot be deleted because it still has associated games.
 */
public class TeamHasGamesException extends GameSchedulerException {

    public TeamHasGamesException(String teamName) {
        super(buildMessage(teamName));
    }

    private static String buildMessage(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return "Cannot delete this team because it has associated scheduled or completed games.";
        } else {
            return "Cannot delete team '" + teamName + "' because it has associated scheduled or completed games.";
        }
    }
}
