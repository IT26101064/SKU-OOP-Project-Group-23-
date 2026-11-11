package com.cinereserve.seat.controller;

import com.cinereserve.seat.model.CinemaHall;
import com.cinereserve.seat.model.Seat;
import com.cinereserve.seat.model.SeatLayout;
import com.cinereserve.seat.service.SeatBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * MEMBER 3 - SEAT BOOKING & AUDITORIUM CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. Visual Cinema Seat Layout (Read) -> /seats/layout
 * 2. Real-Time Seat Hold / Lock & Collision Prevention (Create) -> /seats/lock
 * 3. Cinema Hall Specs & Multiplier Registry (Read/Update) -> /seats/halls
 * 4. Release / Cancel Held Seats (Delete) -> /seats/release
 */
@Controller
@RequestMapping("/seats")
public class SeatBookingController {

    private final SeatBookingService seatBookingService = new SeatBookingService();

    // UI 1: Interactive Cinema Screen & Real-time Seat Grid (Read)
    @GetMapping("/layout")
    public String viewSeatLayout(@RequestParam(value = "hallId", defaultValue = "HAL-001") String hallId,
                                 @RequestParam(value = "showtimeId", defaultValue = "SHW-DEMO-001") String showtimeId,
                                 Model model) {
        SeatLayout layout = seatBookingService.getSeatLayoutForShowtime(hallId, showtimeId);
        List<CinemaHall> halls = seatBookingService.getAllHalls();

        Optional<CinemaHall> currentHall = seatBookingService.getHallById(hallId);

        model.addAttribute("hallId", hallId);
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("halls", halls);
        model.addAttribute("currentHall", currentHall.orElse(null));
        model.addAttribute("seats", layout.getAllSeats().values());
        model.addAttribute("asciiMap", layout.renderAsciiMap());
        return "seat/seat-layout";
    }

    // UI 2: Real-time Seat Reservation & Lock Form (Create)
    @GetMapping("/lock")
    public String showSeatLockForm(@RequestParam(value = "hallId", defaultValue = "HAL-001") String hallId,
                                   @RequestParam(value = "showtimeId", defaultValue = "SHW-DEMO-001") String showtimeId,
                                   Model model) {
        model.addAttribute("hallId", hallId);
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("halls", seatBookingService.getAllHalls());
        return "seat/lock-seat";
    }

    // Process Seat Lock (With Double-Booking Prevention)
    @PostMapping("/lock")
    public String processSeatLock(@RequestParam("hallId") String hallId,
                                  @RequestParam("showtimeId") String showtimeId,
                                  @RequestParam("seatCodes") String seatCodesInput,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (seatCodesInput == null || seatCodesInput.trim().isEmpty()) {
                throw new IllegalArgumentException("Please enter at least one seat code (e.g. A1, B2)");
            }
            List<String> codes = Arrays.stream(seatCodesInput.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .filter(s -> !s.isEmpty())
                    .toList();

            boolean success = seatBookingService.lockSeats(showtimeId, codes);
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage", 
                        "Seats " + codes + " successfully LOCKED for checkout! Collision prevention verified.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                        "DOUBLE-BOOKING COLLISION: One or more selected seats (" + codes + ") are ALREADY locked or reserved!");
            }
            return "redirect:/seats/layout?hallId=" + hallId + "&showtimeId=" + showtimeId;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/seats/lock?hallId=" + hallId + "&showtimeId=" + showtimeId;
        }
    }

    // Release Locked Seats (Delete / Release)
    @PostMapping("/release")
    public String releaseSeats(@RequestParam("hallId") String hallId,
                               @RequestParam("showtimeId") String showtimeId,
                               @RequestParam("seatCodes") String seatCodesInput,
                               RedirectAttributes redirectAttributes) {
        List<String> codes = Arrays.stream(seatCodesInput.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();
        seatBookingService.releaseLockedSeats(showtimeId, codes);
        redirectAttributes.addFlashAttribute("successMessage", "Seats " + codes + " released back to AVAILABLE state.");
        return "redirect:/seats/layout?hallId=" + hallId + "&showtimeId=" + showtimeId;
    }

    // UI 3: Cinema Hall Specifications, Sound Systems, and Pricing Multipliers (Read)
    @GetMapping("/halls")
    public String listHalls(Model model) {
        List<CinemaHall> halls = seatBookingService.getAllHalls();
        model.addAttribute("halls", halls);
        return "seat/halls";
    }
}
