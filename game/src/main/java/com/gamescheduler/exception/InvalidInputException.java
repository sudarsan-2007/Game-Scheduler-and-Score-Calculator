package com.gamescheduler.exception;

/**
 * Thrown when user-provided input fails validation
 * (e.g., blank names, negative IDs, bad date/time format, same home/away team).
 */
public class InvalidInputException extends GameSchedulerException {

    public InvalidInputException(String fieldName, String reason) {
        super(buildMessage(fieldName, reason));
    }

    public InvalidInputException(String message) {
        super(message);
    }

    private static String buildMessage(String fieldName, String reason) {
        if (fieldName == null || fieldName.isBlank()) {
            if (reason == null || reason.isBlank()) {
                return "Invalid input provided.";
            } else {
                return "Invalid input: " + reason;
            }
        } else {
            if (reason == null || reason.isBlank()) {
                return "Invalid value for field '" + fieldName + "'.";
            } else {
                return "Invalid value for '" + fieldName + "': " + reason;
            }
        }
    }
}
