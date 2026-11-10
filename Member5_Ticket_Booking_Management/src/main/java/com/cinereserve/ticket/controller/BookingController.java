package com.cinereserve.ticket.controller;

import com.cinereserve.ticket.model.*;
import com.cinereserve.ticket.service.BookingService;
import com.cinereserve.user.service.UserService;
import com.cinereserve.showtime.service.ShowtimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * ============================================================================
 * MEMBER 5 - TICKET BOOKING CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. Book New Movie Ticket (Create) -> /bookings/new
 * 2. Customer Booking E-Ticket & Receipt (Read) -> /bookings/{ref}
 * 3. Master Bookings Registry & Cancellation (Read/Delete) -> /bookings
 */
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService = new BookingService();
    private final UserService userService = new UserService();
    private final ShowtimeService showtimeService = new ShowtimeService();

    // UI 3: Master Booking Registry Table (Read)
    @GetMapping
    public String listBookings(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        if (search != null && !search.trim().isEmpty()) {
            String s = search.toLowerCase();
            bookings = bookings.stream()
                    .filter(b -> b.getBookingRef().toLowerCase().contains(s) ||
                                 b.getUserId().toLowerCase().contains(s) ||
                                 b.getShowtimeId().toLowerCase().contains(s))
                    .toList();
        }
        model.addAttribute("bookings", bookings);
        model.addAttribute("search", search);
        model.addAttribute("totalRevenue", String.format("$%.2f", bookingService.calculateTotalRevenue()));
        return "ticket/bookings-list";
    }

    // UI 1: Ticket Reservation Form (Create)
    @GetMapping("/new")
    public String showBookingForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("showtimes", showtimeService.getActiveShowtimes());
        return "ticket/book-ticket";
    }

    // Process Booking Form
    @PostMapping("/new")
    public String createBooking(@RequestParam("userId") String userId,
                                @RequestParam("showtimeId") String showtimeId,
                                @RequestParam("seatNumbers") String seatNumbersInput,
                                @RequestParam("ticketType") String ticketType,
                                @RequestParam(value = "basePrice", defaultValue = "12.0") double basePrice,
                                @RequestParam(value = "hallMultiplier", defaultValue = "1.0") double hallMultiplier,
                                RedirectAttributes redirectAttributes) {
        try {
            if (seatNumbersInput == null || seatNumbersInput.trim().isEmpty()) {
                throw new IllegalArgumentException("Seat numbers are required (e.g. A1, A2)");
            }

            String[] seatArray = seatNumbersInput.split(",");
            List<Ticket> tickets = new ArrayList<>();

            for (String s : seatArray) {
                String seatNum = s.trim().toUpperCase();
                if (seatNum.isEmpty()) continue;
                String ticketId = "TCK-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

                if ("VIP".equalsIgnoreCase(ticketType)) {
                    tickets.add(new VIPTicket(ticketId, showtimeId, seatNum, basePrice));
                } else if ("STUDENT".equalsIgnoreCase(ticketType)) {
                    tickets.add(new StudentTicket(ticketId, showtimeId, seatNum, basePrice));
                } else {
                    tickets.add(new StandardTicket(ticketId, showtimeId, seatNum, basePrice));
                }
            }

            // Customer tier discount
            double discountRate = 0.05; // 5% default regular discount
            var userOpt = userService.getUserById(userId);
            if (userOpt.isPresent()) {
                discountRate = userOpt.get().calculateDiscount(100.0) / 100.0;
            }

            Booking booking = bookingService.createBooking(userId, showtimeId, tickets, basePrice, hallMultiplier, discountRate);
            redirectAttributes.addFlashAttribute("successMessage", "Booking created! Reference: " + booking.getBookingRef());
            return "redirect:/bookings/" + booking.getBookingRef();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/bookings/new";
        }
    }

    // UI 2: E-Ticket Summary & Invoice Preview (Read)
    @GetMapping("/{ref}")
    public String viewBookingSummary(@PathVariable("ref") String ref, Model model, RedirectAttributes redirectAttributes) {
        Optional<Booking> bookingOpt = bookingService.getBookingByRef(ref);
        if (bookingOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Booking not found for reference: " + ref);
            return "redirect:/bookings";
        }
        model.addAttribute("booking", bookingOpt.get());
        return "ticket/booking-summary";
    }

    // Cancel Booking (Delete / Cancel)
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean cancelled = bookingService.cancelBooking(id);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking " + id + " has been cancelled.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel booking " + id);
        }
        return "redirect:/bookings";
    }
}
