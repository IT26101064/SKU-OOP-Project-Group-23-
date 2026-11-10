package com.cinereserve.seat;

import com.cinereserve.seat.model.*;
import com.cinereserve.seat.service.SeatBookingService;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================================
 * SE1020 OBJECT-ORIENTED PROGRAMMING - MEMBER 3 DEMO
 * COMPONENT: SEAT BOOKING & CINEMA AUDITORIUM MANAGEMENT
 * ============================================================================
 * Demonstrates:
 * 1. Abstraction (CinemaHall abstract base class & Repository interface)
 * 2. Inheritance (StandardHall, PremiumHall extending CinemaHall)
 * 3. Polymorphism (getHallPriceMultiplier, getSoundSystem, getAmenities)
 * 4. Encapsulation (Seat status state machine: AVAILABLE -> LOCKED -> RESERVED)
 * 5. Persistence (Thread-safe File I/O into data/screens.txt)
 */
public class SeatBookingManagementMain {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("       CINERESERVE - SEAT BOOKING & HALL MANAGEMENT SYSTEM (MEMBER 3)          ");
        System.out.println("================================================================================");

        SeatBookingService service = new SeatBookingService();

        System.out.println("\n--- [STEP 1: Cinema Auditorium Specifications & Polymorphism] ---");
        List<CinemaHall> halls = service.getAllHalls();
        for (CinemaHall h : halls) {
            System.out.println("Hall: " + h.getName());
            System.out.println("  Auditorium Tier : " + h.getHallType());
            System.out.printf("  Multiplier      : %.2fx\n", h.getHallPriceMultiplier());
            System.out.println("  Sound System    : " + h.getSoundSystem());
            System.out.println("  Screen Hardware : " + h.getScreenType());
            System.out.println("  Auditorium Specs: " + h.getAmenities());
            System.out.println("  Capacity        : " + h.getTotalSeats() + " seats (" + h.getVipRowsCount() + " VIP rows)");
            System.out.println("  -------------------------------------------------------------");
        }

        System.out.println("\n--- [STEP 2: Rendering Dynamic ASCII Cinema Seat Layout] ---");
        String testShowtimeId = "SHW-DEMO-001";
        String hallId = "HAL-001"; // Premium Dolby Atmos Hall

        // Pre-reserve some seats to demonstrate realistic occupancy
        service.confirmSeatsReservation(testShowtimeId, Arrays.asList("A1", "A2", "C5", "C6"));
        // Hold/lock a seat in real-time checkout
        service.lockSeats(testShowtimeId, Arrays.asList("B3", "B4"));

        SeatLayout layout = service.getSeatLayoutForShowtime(hallId, testShowtimeId);
        System.out.println(layout.renderAsciiMap());

        System.out.println("\n--- [STEP 3: Double-Booking Prevention & Concurrency Verification] ---");
        System.out.print("Attempting to lock 'B3' (already held) -> ");
        boolean locked = service.lockSeats(testShowtimeId, Arrays.asList("B3"));
        System.out.println(locked ? "UNEXPECTED SUCCESS" : "CORRECTLY PREVENTED (Seat Collision Detected!)");

        System.out.print("Attempting to lock 'D7' (free standard seat) -> ");
        boolean lockFree = service.lockSeats(testShowtimeId, Arrays.asList("D7"));
        System.out.println(lockFree ? "SUCCESS (Seat held for checkout!)" : "FAILED");

        System.out.println("\n================================================================================");
        System.out.println("  MEMBER 3 MODULE READY: All OOP concepts validated and verified!              ");
        System.out.println("================================================================================");
    }
}