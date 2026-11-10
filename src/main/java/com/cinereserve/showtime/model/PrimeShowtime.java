package com.cinereserve.showtime.model;

/**
 * Prime peak-hour screening (19:30 onwards / Prime time blockbusters).
 * 1.20x peak demand multiplier.
 */
public class PrimeShowtime extends Showtime {
    private static final long serialVersionUID = 1L;

    public PrimeShowtime(String showtimeId, String movieId, String hallId,
                         String showDate, String startTime, String endTime,
                         double basePrice, String status) {
        super(showtimeId, movieId, hallId, showDate, startTime, endTime, basePrice, status);
    }

    @Override
    public double calculateTimeSlotMultiplier() {
        return 1.20; // 20% peak demand surcharge
    }

    @Override
    public String getSlotBadge() {
        return "PRIME TIME (PEAK)";
    }

    @Override
    public boolean isPeakHour() {
        return true;
    }

    @Override
    public String getSlotDescription() {
        return "Prime-time high demand screening with full theater atmosphere.";
    }
}