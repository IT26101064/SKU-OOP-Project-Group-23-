package com.cinereserve.seat.model;

/**
 * Standard Hall implementation with 1.0x baseline pricing and Dolby 7.1.
 */
public class StandardHall extends CinemaHall {
    private static final long serialVersionUID = 1L;

    public StandardHall() {
        super();
        setHallType(TYPE_STANDARD);
    }

    public StandardHall(String hallId, String name, int totalRows, int seatsPerRow, int vipRowsCount) {
        super(hallId, name, TYPE_STANDARD, totalRows, seatsPerRow, vipRowsCount);
    }

    @Override
    public double getHallPriceMultiplier() {
        return 1.00; // Baseline multiplier
    }

    @Override
    public String getSoundSystem() {
        return "Dolby Digital 7.1 Surround";
    }

    @Override
    public String getScreenType() {
        return "Digital High-Gain White Matte Screen";
    }

    @Override
    public String getAmenities() {
        return "Ergonomic rocker seating, cup holders, air conditioning";
    }
}