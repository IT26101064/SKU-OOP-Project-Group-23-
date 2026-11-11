package com.cinereserve.seat.util;

import java.util.regex.Pattern;

/**
 * ============================================================================
 * MEMBER 3: SEAT BOOKING & AUDITORIUM MANAGEMENT SYSTEM
 * ============================================================================
 * Defensive input validation utility for auditorium seat coordinates and layouts.
 */
public final class SeatValidationUtil {

    private static final Pattern SEAT_CODE_PATTERN = Pattern.compile("^[A-Z]\\d{1,2}$");

    private SeatValidationUtil() {
        // Utility class
    }

    /**
     * Validates alphanumeric seat code format (e.g., A1, B12).
     */
    public static boolean isValidSeatCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return SEAT_CODE_PATTERN.matcher(code.trim().toUpperCase()).matches();
    }

    /**
     * Extracts row character from seat code.
     */
    public static char extractRow(String code) {
        if (!isValidSeatCode(code)) {
            throw new IllegalArgumentException("Invalid seat code format: " + code);
        }
        return Character.toUpperCase(code.trim().charAt(0));
    }

    /**
     * Extracts column number from seat code.
     */
    public static int extractColumn(String code) {
        if (!isValidSeatCode(code)) {
            throw new IllegalArgumentException("Invalid seat code format: " + code);
        }
        return Integer.parseInt(code.trim().substring(1));
    }

    /**
     * Validates row within cinema hall boundaries.
     */
    public static boolean isWithinHallBounds(char row, int col, int totalRows, int totalCols) {
        int rowIndex = row - 'A' + 1;
        return rowIndex >= 1 && rowIndex <= totalRows && col >= 1 && col <= totalCols;
    }
}
