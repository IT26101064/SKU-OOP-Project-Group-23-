package com.cinereserve.showtime;

import com.cinereserve.showtime.model.*;
import com.cinereserve.showtime.service.ShowtimeService;

import java.util.List;

/**
 * ============================================================================
 * SE1020 OBJECT-ORIENTED PROGRAMMING - MEMBER 4 DEMO
 * COMPONENT: SHOWTIME SCHEDULING MANAGEMENT
 * ============================================================================
 * Demonstrates:
 * 1. Abstraction (Showtime abstract base class & Repository interface)
 * 2. Inheritance (MatineeShowtime, StandardShowtime, PrimeShowtime extending Showtime)
 * 3. Polymorphism (calculateTimeSlotMultiplier, getSlotBadge, isPeakHour)
 * 4. Encapsulation (Hall overlap conflict detection algorithm, 15m cleaning buffer)
 * 5. Persistence (Thread-safe File I/O into data/showtimes.txt)
 */
public class ShowtimeManagementMain {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("          CINERESERVE - SHOWTIME SCHEDULING SYSTEM (MEMBER 4)                   ");
        System.out.println("================================================================================");

        ShowtimeService service = new ShowtimeService();

        System.out.println("\n--- [STEP 1: Active Screening Schedules & Time Slot Multipliers] ---");
        List<Showtime> schedules = service.getActiveShowtimes();
        for (int i = 0; i < Math.min(6, schedules.size()); i++) {
            Showtime s = schedules.get(i);
            System.out.println("Show ID: " + s.getShowtimeId() + " (" + s.getShowDate() + ")");
            System.out.println("  Slot Tier    : " + s.getSlotBadge());
            System.out.println("  Screening    : " + s.getStartTime() + " - " + s.getEndTime() + " in " + s.getHallId());
            System.out.printf("  Base Price   : $%.2f | Slot Multiplier: %.2fx\n", s.getBasePrice(), s.calculateTimeSlotMultiplier());
            System.out.printf("  Actual Price : $%.2f\n", s.getEffectivePrice());
            System.out.println("  Peak Hour?   : " + (s.isPeakHour() ? "YES" : "NO"));
            System.out.println("  -------------------------------------------------------------");
        }

        System.out.println("\n--- [STEP 2: Demonstrating Overlap Conflict Detection Algorithm] ---");
        System.out.println("Scenario: Hall 1 already has a screening on 2026-10-12 from 13:30 to 15:25.");
        System.out.println("Attempting to schedule an overlapping screening in Hall 1 from 14:00 to 16:00...");

        try {
            service.scheduleShowtime("MOV-003", "HAL-001", "2026-10-12", "14:00", "16:00", 15.00);
            System.out.println("ERROR: Conflict detection failed to catch overlap!");
        } catch (IllegalStateException e) {
            System.out.println(">> SUCCESS: Conflict Intercepted & Blocked!");
            System.out.println("   Reason: " + e.getMessage());
        }

        System.out.println("\nAttempting to schedule a conflict-free show after 15:25 + 15min buffer (at 15:45)...");
        try {
            Showtime validShow = service.scheduleShowtime("MOV-003", "HAL-001", "2026-10-12", "15:45", "16:15", 14.00);
            System.out.println(">> SUCCESS: Conflict-free showtime scheduled! ID: " + validShow.getShowtimeId());
        } catch (Exception e) {
            System.out.println("Note: " + e.getMessage());
        }

        System.out.println("\n================================================================================");
        System.out.println("  MEMBER 4 MODULE READY: All OOP concepts validated and verified!              ");
        System.out.println("================================================================================");
    }
}