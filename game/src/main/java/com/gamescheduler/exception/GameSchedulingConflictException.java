package com.gamescheduler.exception;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Thrown when a game cannot be scheduled because a team already has
 * an overlapping game at the requested date and time.
 */
public class GameSchedulingConflictException extends GameSchedulerException {

    public GameSchedulingConflictException(String teamRole, LocalDate date, LocalTime time) {
        super(buildMessage(teamRole, date, time));
    }

    private static String buildMessage(String teamRole, LocalDate date, LocalTime time) {
        if (date == null && time == null) {
            return teamRole + " team already has a conflicting game scheduled.";
        } else if (date == null) {
            return teamRole + " team already has a conflicting game scheduled at " + time + ".";
        } else if (time == null) {
            return teamRole + " team already has a conflicting game scheduled on " + date + ".";
        } else {
            return teamRole + " team already has a conflicting game scheduled on " + date + " at " + time + ".";
        }
    }
}
