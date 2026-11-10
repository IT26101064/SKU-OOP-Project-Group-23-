package com.cinereserve.seat.service;

import com.cinereserve.seat.model.*;
import com.cinereserve.seat.repository.CinemaHallRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SeatBookingService {

    private final CinemaHallRepository hallRepository;
    // Maps showtimeId -> Set of reserved seats (confirmed bookings)
    private final Map<String, Set<String>> reservedSeatsMap = new ConcurrentHashMap<>();
    // Maps showtimeId -> Set of currently locked/held seats
    private final Map<String, Set<String>> lockedSeatsMap = new ConcurrentHashMap<>();

    public SeatBookingService() {
        this.hallRepository = new CinemaHallRepository();
    }

    public SeatBookingService(CinemaHallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public SeatLayout getSeatLayoutForShowtime(String hallId, String showtimeId) {
        CinemaHall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new IllegalArgumentException("Cinema Hall not found: " + hallId));

        Set<String> reserved = reservedSeatsMap.getOrDefault(showtimeId, Collections.emptySet());
        Set<String> locked = lockedSeatsMap.getOrDefault(showtimeId, Collections.emptySet());

        return new SeatLayout(hall, reserved, locked);
    }

    public synchronized boolean lockSeats(String showtimeId, List<String> seatCodes) {
        if (seatCodes == null || seatCodes.isEmpty()) return false;

        Set<String> reserved = reservedSeatsMap.computeIfAbsent(showtimeId, k -> ConcurrentHashMap.newKeySet());
        Set<String> locked = lockedSeatsMap.computeIfAbsent(showtimeId, k -> ConcurrentHashMap.newKeySet());

        for (String code : seatCodes) {
            String c = code.toUpperCase();
            if (reserved.contains(c) || locked.contains(c)) {
                return false; // Seat collision: already taken or currently on hold!
            }
        }

        for (String code : seatCodes) {
            locked.add(code.toUpperCase());
        }
        return true;
    }

    public synchronized void releaseLockedSeats(String showtimeId, List<String> seatCodes) {
        Set<String> locked = lockedSeatsMap.get(showtimeId);
        if (locked != null && seatCodes != null) {
            for (String code : seatCodes) {
                locked.remove(code.toUpperCase());
            }
        }
    }

    public synchronized void confirmSeatsReservation(String showtimeId, List<String> seatCodes) {
        Set<String> locked = lockedSeatsMap.get(showtimeId);
        Set<String> reserved = reservedSeatsMap.computeIfAbsent(showtimeId, k -> ConcurrentHashMap.newKeySet());

        if (seatCodes != null) {
            for (String code : seatCodes) {
                String c = code.toUpperCase();
                if (locked != null) locked.remove(c);
                reserved.add(c);
            }
        }
    }

    public List<CinemaHall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Optional<CinemaHall> getHallById(String id) {
        return hallRepository.findById(id);
    }
}