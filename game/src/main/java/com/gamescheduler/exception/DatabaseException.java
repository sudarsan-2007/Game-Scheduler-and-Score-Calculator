package com.gamescheduler.exception;

/**
 * Thrown when a database operation fails unexpectedly (e.g., SQL error,
 * connection failure). Wraps the underlying cause for diagnosis.
 */
public class DatabaseException extends GameSchedulerException {

    public DatabaseException(String operation, Throwable cause) {
        super(buildMessage(operation, cause), cause);
    }

    public DatabaseException(String message) {
        super(message);
    }

    private static String buildMessage(String operation, Throwable cause) {
        if (operation == null || operation.isBlank()) {
            if (cause != null && cause.getMessage() != null) {
                return "Database error: " + cause.getMessage();
            } else {
                return "An unexpected database error occurred.";
            }
        } else {
            if (cause != null && cause.getMessage() != null) {
                return "Database error during '" + operation + "': " + cause.getMessage();
            } else {
                return "Database error during '" + operation + "'.";
            }
        }
    }
}
