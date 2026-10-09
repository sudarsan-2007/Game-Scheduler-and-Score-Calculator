package com.gamescheduler.exception;

/**
 * Base unchecked exception for the Game Scheduler application.
 * All domain-specific exceptions extend this class.
 */
public class GameSchedulerException extends RuntimeException {

    public GameSchedulerException(String message) {
        super(message);
    }

    public GameSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}
