package com.cinereserve.controller;

import com.cinereserve.user.service.UserService;
import com.cinereserve.movie.service.MovieService;
import com.cinereserve.seat.service.SeatBookingService;
import com.cinereserve.showtime.service.ShowtimeService;
import com.cinereserve.ticket.service.BookingService;
import com.cinereserve.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService = new UserService();
    private final MovieService movieService = new MovieService();
    private final SeatBookingService seatBookingService = new SeatBookingService();
    private final ShowtimeService showtimeService = new ShowtimeService();
    private final BookingService bookingService = new BookingService();
    private final PaymentService paymentService = new PaymentService();

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalMovies", movieService.getAllMovies().size());
        model.addAttribute("totalHalls", seatBookingService.getAllHalls().size());
        model.addAttribute("totalShowtimes", showtimeService.getAllShowtimes().size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("totalPayments", paymentService.getAllPayments().size());

        model.addAttribute("nowShowingMovies", movieService.getNowShowingMovies());
        model.addAttribute("activeShowtimes", showtimeService.getActiveShowtimes());
        return "index";
    }
}
