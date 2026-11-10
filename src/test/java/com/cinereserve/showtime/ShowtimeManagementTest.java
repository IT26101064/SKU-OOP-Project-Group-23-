package com.cinereserve.showtime;

import com.cinereserve.showtime.model.*;
import com.cinereserve.showtime.service.ShowtimeService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShowtimeManagementTest {

    private ShowtimeService service;

    @BeforeEach
    void setUp() {
        service = new ShowtimeService();
    }

    @Test
    void testMatineeTimeSlotPolymorphism() {
        Showtime matinee = new MatineeShowtime("S1", "M1", "H1", "2026-05-01", "13:00", "15:00", 20.00, "ACTIVE");
        assertEquals("MATINEE (EARLY BIRD)", matinee.getSlotBadge());
        assertEquals(0.85, matinee.calculateTimeSlotMultiplier());
        assertEquals(17.00, matinee.getEffectivePrice());
        assertFalse(matinee.isPeakHour());
    }

    @Test
    void testPrimeTimeSlotPolymorphism() {
        Showtime prime = new PrimeShowtime("S2", "M1", "H1", "2026-05-01", "20:00", "22:30", 20.00, "ACTIVE");
        assertEquals("PRIME TIME (PEAK)", prime.getSlotBadge());
        assertEquals(1.20, prime.calculateTimeSlotMultiplier());
        assertEquals(24.00, prime.getEffectivePrice());
        assertTrue(prime.isPeakHour());
    }

    @Test
    void testScheduleConflictDetection() {
        String testDate = "2029-01-01";
        String hall = "HAL-TEST-1";
        // Schedule first screening: 10:00 - 12:00
        service.scheduleShowtime("M1", hall, testDate, "10:00", "12:00", 15.00);

        // Attempting to schedule during cleaning buffer (12:05) must fail
        assertThrows(IllegalStateException.class, () -> {
            service.scheduleShowtime("M2", hall, testDate, "12:05", "14:00", 15.00);
        });

        // Scheduling after buffer (12:15 +) must succeed
        assertDoesNotThrow(() -> {
            service.scheduleShowtime("M2", hall, testDate, "12:20", "14:20", 15.00);
        });
    }
}