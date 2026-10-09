package com.gamescheduler.exception;

/**
 * Thrown when a requested game does not exist in the system.
 */
public class GameNotFoundException extends GameSchedulerException {

    public GameNotFoundException(int gameId) {
        super(buildMessage(gameId));
    }

    private static String buildMessage(int gameId) {
        if (gameId <= 0) {
            return "Game ID must be a positive number. Provided: " + gameId;
        } else {
            return "Game with ID " + gameId + " not found.";
        }
    }
}
