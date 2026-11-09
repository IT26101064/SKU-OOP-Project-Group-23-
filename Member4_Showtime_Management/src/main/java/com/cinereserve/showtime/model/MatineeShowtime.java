package com.cinereserve.showtime.model;

/**
 * Matinee screening (Morning / Early Afternoon before 16:00).
 * Offers a 15% discount multiplier (0.85x).
 */
public class MatineeShowtime extends Showtime {
    private static final long serialVersionUID = 1L;

    public MatineeShowtime(String showtimeId, String movieId, String hallId,
                           String showDate, String startTime, String endTime,
                           double basePrice, String status) {
        super(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
    }

    @Override
    public double calculateTimeSlotMultiplier() {
        return 0.85; // 15% off-peak early bird discount
    }

    @Override
    public String getSlotBadge() {
        return "MATINEE (EARLY BIRD)";
    }

    @Override
    public boolean isPeakHour() {
        return false;
    }

    @Override
    public String getSlotDescription() {
        return "Morning & Afternoon discount screening. 15% lower admission price.";
    }
}