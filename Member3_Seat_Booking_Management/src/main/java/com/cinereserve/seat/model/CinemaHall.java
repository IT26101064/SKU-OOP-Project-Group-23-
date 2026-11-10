package com.cinereserve.seat.model;

import java.io.Serializable;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION & ENCAPSULATION
 * =========================================================================
 * Abstract base class representing a cinema auditorium/hall.
 */
public abstract class CinemaHall implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_STANDARD = "STANDARD";
    public static final String TYPE_PREMIUM = "PREMIUM";

    private String hallId;
    private String name;
    private String hallType; // "STANDARD" or "PREMIUM"
    private int totalRows;   // e.g. 8 (Rows A through H)
    private int seatsPerRow; // e.g. 12 (Seats 1 through 12)
    private int vipRowsCount; // e.g. 3 (Rows A through C are VIP)

    public CinemaHall() {}

    public CinemaHall(String hallId, String name, String hallType,
                      int totalRows, int seatsPerRow, int vipRowsCount) {
        this.hallId = hallId;
        this.name = name;
        this.hallType = hallType;
        this.totalRows = totalRows;
        this.seatsPerRow = seatsPerRow;
        this.vipRowsCount = vipRowsCount;
    }

    // =========================================================================
    // ABSTRACT METHODS (ABSTRACTION & POLYMORPHISM)
    // =========================================================================

    public abstract double getHallPriceMultiplier();
    public abstract String getSoundSystem();
    public abstract String getScreenType();
    public abstract String getAmenities();

    public int getTotalSeats() {
        return totalRows * seatsPerRow;
    }

    public boolean isVipSeat(String seatCode) {
        if (seatCode == null || seatCode.isEmpty()) return false;
        char rowLetter = Character.toUpperCase(seatCode.charAt(0));
        int rowIndex = rowLetter - 'A' + 1; // A=1, B=2, C=3
        return rowIndex <= vipRowsCount;
    }

    public String getHallId() { return hallId; }
    public void setHallId(String hallId) { this.hallId = hallId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHallType() { return hallType; }
    public void setHallType(String hallType) { this.hallType = hallType; }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

    public int getSeatsPerRow() { return seatsPerRow; }
    public void setSeatsPerRow(int seatsPerRow) { this.seatsPerRow = seatsPerRow; }

    public int getVipRowsCount() { return vipRowsCount; }
    public void setVipRowsCount(int vipRowsCount) { this.vipRowsCount = vipRowsCount; }

    @Override
    public String toString() {
        return String.format("[%s] %s (ID: %s) | Rows: %d | Seats/Row: %d | Total: %d | VIP Rows: %d",
                hallType, name, hallId, totalRows, seatsPerRow, getTotalSeats(), vipRowsCount);
    }
}