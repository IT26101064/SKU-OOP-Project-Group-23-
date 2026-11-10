package com.cinereserve.seat.model;

import java.io.Serializable;

public class Seat implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { AVAILABLE, LOCKED, RESERVED }
    public enum Tier { STANDARD, VIP }

    private String seatCode; // e.g. "A1", "B5"
    private char rowLetter;
    private int seatNumber;
    private Tier tier;
    private Status status;

    public Seat(String seatCode, char rowLetter, int seatNumber, Tier tier, Status status) {
        this.seatCode = seatCode;
        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
        this.tier = tier;
        this.status = status;
    }

    public boolean isAvailable() { return status == Status.AVAILABLE; }
    public boolean isLocked() { return status == Status.LOCKED; }
    public boolean isReserved() { return status == Status.RESERVED; }
    public boolean isVip() { return tier == Tier.VIP; }

    public String getSeatCode() { return seatCode; }
    public char getRowLetter() { return rowLetter; }
    public int getSeatNumber() { return seatNumber; }
    public Tier getTier() { return tier; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return seatCode + "(" + tier + ":" + status + ")";
    }
}