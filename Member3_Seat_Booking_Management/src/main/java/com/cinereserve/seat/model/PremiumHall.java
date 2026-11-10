package com.cinereserve.seat.model;

/**
 * Premium IMAX / Dolby Atmos Hall implementation with 1.35x luxury multiplier.
 */
public class PremiumHall extends CinemaHall {
    private static final long serialVersionUID = 1L;

    public PremiumHall() {
        super();
        setHallType(TYPE_PREMIUM);
    }

    public PremiumHall(String hallId, String name, int totalRows, int seatsPerRow, int vipRowsCount) {
        super(hallId, name, TYPE_PREMIUM, totalRows, seatsPerRow, vipRowsCount);
    }

    @Override
    public double getHallPriceMultiplier() {
        return 1.35; // 35% premium acoustic & visual markup
    }

    @Override
    public String getSoundSystem() {
        return "Dolby Atmos 64-Channel Spatial Audio";
    }

    @Override
    public String getScreenType() {
        return "Curved Giant Silver Screen with Dual 4K Laser";
    }

    @Override
    public String getAmenities() {
        return "Motorized full leather recliners, personal swivel tables, VIP in-seat service";
    }
}