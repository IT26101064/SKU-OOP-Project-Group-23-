package com.cinereserve.seat.model;

/**
 * ============================================================================
 * MEMBER 3: SEAT BOOKING & AUDITORIUM MANAGEMENT SYSTEM
 * ============================================================================
 * Lifecycle state representation for cinema auditorium seats.
 */
public enum SeatStatus {
    AVAILABLE("Available", "bg-success text-white"),
    LOCKED("Held / In-Checkout", "bg-warning text-dark"),
    RESERVED("Reserved / Booked", "bg-danger text-white"),
    MAINTENANCE("Out of Order", "bg-secondary text-white");

    private final String displayName;
    private final String badgeCssClass;

    SeatStatus(String displayName, String badgeCssClass) {
        this.displayName = displayName;
        this.badgeCssClass = badgeCssClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeCssClass() {
        return badgeCssClass;
    }

    public boolean isBookable() {
        return this == AVAILABLE;
    }
}
