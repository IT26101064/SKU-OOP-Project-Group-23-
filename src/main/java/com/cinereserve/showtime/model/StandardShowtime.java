package com.cinereserve.showtime.model;

/**
 * Standard regular evening screening (16:00 to 19:30).
 * Baseline 1.00x multiplier.
 */
public class StandardShowtime extends Showtime {
    private static final long serialVersionUID = 1L;

    public StandardShowtime(String showtimeId, String movieId, String hallId,
                            String showDate, String startTime, String endTime,
                            double basePrice, String status) {
        super(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
    }

    @Override
    public double calculateTimeSlotMultiplier() {
        return 1.00;
    }

    @Override
    public String getSlotBadge() {
        return "STANDARD EVENING";
    }

    @Override
    public boolean isPeakHour() {
        return false;
    }

    @Override
    public String getSlotDescription() {
        return "Standard twilight screening with regular admission pricing.";
    }
}