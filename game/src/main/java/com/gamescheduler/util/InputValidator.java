package com.gamescheduler.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InputValidator {

    public static void validateTeamName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty.");
        }
        if (name.trim().length() < 2 || name.trim().length() > 100) {
            throw new IllegalArgumentException("Team name must be between 2 and 100 characters.");
        }
    }

    public static void validateCoachName(String coachName) {
        if (coachName == null || coachName.trim().isEmpty()) {
            throw new IllegalArgumentException("Coach name cannot be empty.");
        }
        if (coachName.trim().length() < 2 || coachName.trim().length() > 100) {
            throw new IllegalArgumentException("Coach name must be between 2 and 100 characters.");
        }
    }

    public static void validateVenue(String venue) {
        if (venue == null || venue.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue cannot be empty.");
        }
        if (venue.trim().length() < 2 || venue.trim().length() > 150) {
            throw new IllegalArgumentException("Venue must be between 2 and 150 characters.");
        }
    }

    public static void validatePositiveId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be a positive integer.");
        }
    }

    public static void validateTeamsNotSame(int homeTeamId, int awayTeamId) {
        if (homeTeamId == awayTeamId) {
            throw new IllegalArgumentException("A team cannot play against itself (Home Team ID and Away Team ID are identical).");
        }
    }

    public static void validateScore(int score, String teamRole) {
        if (score < 0) {
            throw new IllegalArgumentException(teamRole + " score cannot be negative. Entered score: " + score);
        }
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format '" + dateStr + "'. Please use YYYY-MM-DD format (e.g., 2026-10-15).");
        }
    }

    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Time cannot be empty.");
        }
        try {
            String trimmed = timeStr.trim();
            if (trimmed.length() == 5) {
                trimmed = trimmed + ":00";
            }
            return LocalTime.parse(trimmed);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format '" + timeStr + "'. Please use HH:MM format (e.g., 15:30).");
        }
    }
}
