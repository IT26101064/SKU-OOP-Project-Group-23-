package com.cinereserve.showtime.controller;

import com.cinereserve.showtime.model.Showtime;
import com.cinereserve.showtime.service.ShowtimeService;
import com.cinereserve.movie.service.MovieService;
import com.cinereserve.seat.service.SeatBookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * ============================================================================
 * MEMBER 4 - SHOWTIME MANAGEMENT CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. Schedule New Screening with Collision Prevention (Create) -> /showtimes/add
 * 2. Daily Screening Timetable (Read) -> /showtimes
 * 3. Cancel / Reschedule Screening (Update/Delete) -> /showtimes/cancel/{id}
 */
@Controller
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService = new ShowtimeService();
    private final MovieService movieService = new MovieService();
    private final SeatBookingService seatBookingService = new SeatBookingService();

    // UI 2: Daily Screening Schedule Table (Read)
    @GetMapping
    public String viewSchedule(@RequestParam(value = "movieId", required = false) String movieId,
                               @RequestParam(value = "hallId", required = false) String hallId,
                               Model model) {
        List<Showtime> showtimes;
        if (movieId != null && !movieId.trim().isEmpty() && !"ALL".equalsIgnoreCase(movieId)) {
            showtimes = showtimeService.getShowtimesByMovie(movieId);
        } else if (hallId != null && !hallId.trim().isEmpty() && !"ALL".equalsIgnoreCase(hallId)) {
            showtimes = showtimeService.getShowtimesByHall(hallId);
        } else {
            showtimes = showtimeService.getAllShowtimes();
        }

        model.addAttribute("showtimes", showtimes);
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("halls", seatBookingService.getAllHalls());
        model.addAttribute("selectedMovie", movieId);
        model.addAttribute("selectedHall", hallId);
        return "showtime/schedule";
    }

    // UI 1: Schedule Screening Form with Conflict Checking (Create Form)
    @GetMapping("/add")
    public String showAddShowtimeForm(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("halls", seatBookingService.getAllHalls());
        return "showtime/add-showtime";
    }

    // Process Add Showtime Form
    @PostMapping("/add")
    public String scheduleShowtime(@RequestParam("movieId") String movieId,
                                  @RequestParam("hallId") String hallId,
                                  @RequestParam("showDate") String showDate,
                                  @RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime,
                                  @RequestParam("basePrice") double basePrice,
                                  RedirectAttributes redirectAttributes) {
        try {
            Showtime showtime = showtimeService.scheduleShowtime(movieId, hallId, showDate, startTime, endTime, basePrice);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Showtime scheduled successfully! ID: " + showtime.getShowtimeId() + 
                    " (" + showtime.getSlotBadge() + ", Price: $" + String.format("%.2f", showtime.getBasePrice()) + ")");
            return "redirect:/showtimes";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/showtimes/add";
        }
    }

    // UI 3: Cancel or Manage Showtime (Delete / Update)
    @PostMapping("/cancel/{id}")
    public String cancelShowtime(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean cancelled = showtimeService.cancelShowtime(id);
        if (cancelled) {
            redirectAttributes.addFlashAttribute("successMessage", "Showtime " + id + " has been CANCELLED.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel showtime: " + id);
        }
        return "redirect:/showtimes";
    }
}
