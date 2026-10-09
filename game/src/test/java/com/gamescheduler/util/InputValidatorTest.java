package com.gamescheduler.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    @Test
    @DisplayName("Should validate valid team name")
    void testValidTeamName() {
        assertDoesNotThrow(() -> InputValidator.validateTeamName("Thunder Strikers"));
    }

    @Test
    @DisplayName("Should throw exception for blank team name")
    void testBlankTeamName() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateTeamName("   "));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateTeamName(null));
    }

    @Test
    @DisplayName("Should parse valid date strings")
    void testValidDateParsing() {
        LocalDate date = InputValidator.parseDate("2026-10-15");
        assertEquals(2026, date.getYear());
        assertEquals(10, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    @DisplayName("Should throw exception for invalid date formats")
    void testInvalidDateParsing() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.parseDate("15-10-2026"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.parseDate("2026/10/15"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.parseDate("invalid-date"));
    }

    @Test
    @DisplayName("Should parse valid time strings")
    void testValidTimeParsing() {
        LocalTime time = InputValidator.parseTime("15:30");
        assertEquals(15, time.getHour());
        assertEquals(30, time.getMinute());

        LocalTime timeWithSeconds = InputValidator.parseTime("18:45:30");
        assertEquals(18, timeWithSeconds.getHour());
        assertEquals(45, timeWithSeconds.getMinute());
    }

    @Test
    @DisplayName("Should throw exception for invalid time formats")
    void testInvalidTimeParsing() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.parseTime("25:00"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.parseTime("invalid-time"));
    }

    @Test
    @DisplayName("Should reject negative scores")
    void testNegativeScoreValidation() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateScore(-1, "Home Team"));
        assertDoesNotThrow(() -> InputValidator.validateScore(0, "Home Team"));
        assertDoesNotThrow(() -> InputValidator.validateScore(5, "Away Team"));
    }
}
