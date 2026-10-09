package com.gamescheduler.model;

public enum GameStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED;

    public static GameStatus fromString(String statusStr) {
        if (statusStr == null) {
            return SCHEDULED;
        }
        try {
            return GameStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SCHEDULED;
        }
    }
}
