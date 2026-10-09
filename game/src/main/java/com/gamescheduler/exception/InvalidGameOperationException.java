package com.gamescheduler.exception;

/**
 * Thrown when an operation is attempted on a game that is in an invalid state
 * for that operation (e.g., modifying or cancelling a completed game).
 */
public class InvalidGameOperationException extends GameSchedulerException {

    public InvalidGameOperationException(String message) {
        super(message);
    }
}
