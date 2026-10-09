package com.gamescheduler.exception;

/**
 * Thrown when attempting to add or rename a team with a name that is already in use.
 */
public class TeamAlreadyExistsException extends GameSchedulerException {

    public TeamAlreadyExistsException(String teamName) {
        super(buildMessage(teamName));
    }

    private static String buildMessage(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return "A team with that name already exists.";
        } else {
            return "Team with name '" + teamName + "' already exists.";
        }
    }
}
