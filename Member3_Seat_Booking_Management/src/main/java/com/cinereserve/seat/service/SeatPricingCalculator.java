package com.cinereserve.seat.service;

import com.cinereserve.seat.model.CinemaHall;

/**
 * ============================================================================
 * MEMBER 3: SEAT BOOKING & AUDITORIUM MANAGEMENT SYSTEM
 * ============================================================================
 * Dynamic seat pricing engine combining auditorium tier multiplier
 * and luxury VIP recliner surcharges.
 */
public class SeatPricingCalculator {

    private static final double VIP_RECLINER_SURCHARGE = 4.50;

    /**
     * Calculates the dynamic price for a specific seat in a given cinema hall.
     *
     * @param basePrice  Base movie ticket price
     * @param hall       CinemaHall instance (Polymorphic multiplier)
     * @param isVipSeat  Whether the seat is located in the VIP premium row
     * @return Final computed seat reservation price
     */
    public double calculateSeatPrice(double basePrice, CinemaHall hall, boolean isVipSeat) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        if (hall == null) {
            throw new IllegalArgumentException("CinemaHall cannot be null");
        }

        // Apply polymorphic hall multiplier (Standard 1.0x, Premium 1.35x)
        double hallAdjustedPrice = basePrice * hall.getHallPriceMultiplier();

        // Apply VIP Recliner surcharge if applicable
        if (isVipSeat) {
            hallAdjustedPrice += VIP_RECLINER_SURCHARGE;
        }

        return Math.round(hallAdjustedPrice * 100.0) / 100.0;
    }

    public double getVipReclinerSurcharge() {
        return VIP_RECLINER_SURCHARGE;
    }
}
