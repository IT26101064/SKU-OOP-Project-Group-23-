package com.cinereserve.seat.model;

import java.util.*;

public class SeatLayout {
    private final CinemaHall hall;
    private final Map<String, Seat> seatMatrix;

    public SeatLayout(CinemaHall hall, Set<String> reservedSeats, Set<String> lockedSeats) {
        this.hall = hall;
        this.seatMatrix = new LinkedHashMap<>();

        for (int r = 0; r < hall.getTotalRows(); r++) {
            char rowChar = (char) ('A' + r);
            boolean isVipRow = (r + 1) <= hall.getVipRowsCount();
            Seat.Tier tier = isVipRow ? Seat.Tier.VIP : Seat.Tier.STANDARD;

            for (int s = 1; s <= hall.getSeatsPerRow(); s++) {
                String code = rowChar + String.valueOf(s);
                Seat.Status status = Seat.Status.AVAILABLE;
                if (reservedSeats != null && reservedSeats.contains(code)) {
                    status = Seat.Status.RESERVED;
                } else if (lockedSeats != null && lockedSeats.contains(code)) {
                    status = Seat.Status.LOCKED;
                }
                seatMatrix.put(code, new Seat(code, rowChar, s, tier, status));
            }
        }
    }

    public Seat getSeat(String code) {
        return seatMatrix.get(code != null ? code.toUpperCase() : "");
    }

    public Map<String, Seat> getAllSeats() {
        return Collections.unmodifiableMap(seatMatrix);
    }

    public String renderAsciiMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n          ===================================================\n");
        sb.append("          |                 CINEMA SCREEN                   |\n");
        sb.append("          ===================================================\n\n");

        for (int r = 0; r < hall.getTotalRows(); r++) {
            char rowChar = (char) ('A' + r);
            boolean isVip = (r + 1) <= hall.getVipRowsCount();
            sb.append(String.format("Row %c %-5s: ", rowChar, (isVip ? "[VIP]" : "[STD]")));

            for (int s = 1; s <= hall.getSeatsPerRow(); s++) {
                String code = rowChar + String.valueOf(s);
                Seat seat = seatMatrix.get(code);
                if (seat.isReserved()) {
                    sb.append(" [ X ]"); // Occupied
                } else if (seat.isLocked()) {
                    sb.append(" [ ! ]"); // Held
                } else if (seat.isVip()) {
                    sb.append(String.format(" [%2s*]", code)); // VIP Available
                } else {
                    sb.append(String.format(" [%2s ]", code)); // Standard Available
                }
            }
            sb.append("\n");
        }
        sb.append("\nLegend: [code ] = Available  [* ] = VIP  [! ] = Hold/Locked  [ X ] = Occupied\n");
        return sb.toString();
    }
}