package com.cinereserve.seat;

import com.cinereserve.seat.model.*;
import com.cinereserve.seat.service.SeatBookingService;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SeatBookingManagementTest {

    private SeatBookingService service;

    @BeforeEach
    void setUp() {
        service = new SeatBookingService();
    }

    @Test
    void testStandardHallPolymorphism() {
        CinemaHall std = new StandardHall("HAL-01", "Main Hall", 8, 10, 2);
        assertEquals("STANDARD", std.getHallType());
        assertEquals(1.00, std.getHallPriceMultiplier());
        assertEquals(80, std.getTotalSeats());
        assertTrue(std.isVipSeat("A5"));
        assertTrue(std.isVipSeat("B1"));
        assertFalse(std.isVipSeat("C1"));
    }

    @Test
    void testPremiumHallPolymorphism() {
        CinemaHall prem = new PremiumHall("HAL-02", "IMAX Hall", 10, 12, 3);
        assertEquals("PREMIUM", prem.getHallType());
        assertEquals(1.35, prem.getHallPriceMultiplier());
        assertEquals(120, prem.getTotalSeats());
        assertTrue(prem.isVipSeat("C10"));
        assertFalse(prem.isVipSeat("D1"));
    }

    @Test
    void testSeatLockingAndCollisionPrevention() {
        String showId = "TEST-SHOW-99";
        assertTrue(service.lockSeats(showId, Arrays.asList("A1", "A2")));
        // Locking already held seat must fail
        assertFalse(service.lockSeats(showId, Arrays.asList("A1")));
        // Release seat
        service.releaseLockedSeats(showId, Arrays.asList("A1"));
        // Now locking A1 must succeed
        assertTrue(service.lockSeats(showId, Arrays.asList("A1")));
    }
}