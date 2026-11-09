package com.cinereserve.showtime.model;

import java.io.Serializable;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION & ENCAPSULATION
 * =========================================================================
 * Abstract base class representing a scheduled cinema screening.
 */
public abstract class Showtime implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private String showtimeId;
    private String movieId;
    private String hallId;
    private String showDate;  // "YYYY-MM-DD"
    private String startTime; // "HH:mm" (24h)
    private String endTime;   // "HH:mm" (24h)
    private double basePrice;
    private String status;    // "ACTIVE", "CANCELLED"

    public Showtime() {}

    public Showtime(String showtimeId, String movieId, String hallId,
                    String showDate, String startTime, String endTime,
                    double basePrice, String status) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.hallId = hallId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.basePrice = basePrice;
        this.status = status != null ? status : STATUS_ACTIVE;
    }

    // =========================================================================
    // ABSTRACT METHODS (ABSTRACTION & POLYMORPHISM)
    // =========================================================================

    public abstract double calculateTimeSlotMultiplier();
    public abstract String getSlotBadge();
    public abstract boolean isPeakHour();
    public abstract String getSlotDescription();

    public boolean isActive() {
        return STATUS_ACTIVE.equalsIgnoreCase(status);
    }

    public double getEffectivePrice() {
        return basePrice * calculateTimeSlotMultiplier();
    }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getHallId() { return hallId; }
    public void setHallId(String hallId) { this.hallId = hallId; }

    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Movie: %s | Hall: %s | Date: %s | %s - %s | Price: $%.2f",
                getSlotBadge(), showtimeId, movieId, hallId, showDate, startTime, endTime, getEffectivePrice());
    }
}