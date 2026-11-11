package com.cinereserve.seat.model;

/**
 * ============================================================================
 * MEMBER 3: SEAT BOOKING & AUDITORIUM MANAGEMENT SYSTEM
 * ============================================================================
 * Custom domain exception thrown when concurrent users attempt to lock or
 * reserve a seat that is already held or reserved (double-booking avoidance).
 */
public class SeatCollisionException extends RuntimeException {

    private final String seatCode;
    private final String showtimeId;

    public SeatCollisionException(String seatCode, String showtimeId) {
        super(String.format("SEAT COLLISION DETECTED: Seat '%s' for screening '%s' is already locked or booked!",
                seatCode, showtimeId));
        this.seatCode = seatCode;
        this.showtimeId = showtimeId;
    }

    public String getSeatCode() {
        return seatCode;
    }

    public String getShowtimeId() {
        return showtimeId;
    }
}
