package com.gamescheduler.exception;

/**
 * Thrown when a requested team does not exist in the system.
 * Accepts either an integer team ID or a String team name.
 */
public class TeamNotFoundException extends GameSchedulerException {

    public TeamNotFoundException(Object identifier) {
        super(buildMessage(identifier));
    }

    private static String buildMessage(Object identifier) {
        if (identifier instanceof Integer) {
            return "Team with ID " + identifier + " not found.";
        } else if (identifier instanceof String) {
            return "Team with name '" + identifier + "' not found.";
        } else {
            return "Team not found.";
        }
    }
}
