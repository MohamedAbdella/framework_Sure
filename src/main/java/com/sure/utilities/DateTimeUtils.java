package com.sure.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class providing common date and time helper methods used across
 * the framework. This centralises format handling and avoids scattering
 * {@link java.time} usage throughout the test code.
 */
public final class DateTimeUtils {
    private DateTimeUtils() {
    }

    /**
     * Returns today's date formatted using the supplied pattern.
     *
     * @param pattern the {@link java.time.format.DateTimeFormatter} pattern
     * @return formatted date string
     */
    public static String today(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns the current time formatted using the supplied pattern.
     *
     * @param pattern the formatter pattern
     * @return formatted time string
     */
    public static String now(String pattern) {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns a future date relative to today formatted with the given pattern.
     *
     * @param daysFromNow number of days to add to today
     * @param pattern     the desired date format
     * @return formatted future date string
     */
    public static String futureDate(int daysFromNow, String pattern) {
        return LocalDate.now().plusDays(daysFromNow)
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns the current timestamp using ISO-8601 format.
     */
    public static String timestamp() {
        return LocalDateTime.now().toString();
    }
}
