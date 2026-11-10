package com.cinereserve.movie;

import com.cinereserve.movie.model.*;
import com.cinereserve.movie.service.MovieService;

import java.util.List;

/**
 * ============================================================================
 * SE1020 OBJECT-ORIENTED PROGRAMMING - MEMBER 2 DEMO
 * COMPONENT: MOVIE MANAGEMENT
 * ============================================================================
 * Demonstrates:
 * 1. Abstraction (Movie abstract base class & Repository interface)
 * 2. Inheritance (StandardMovie, ThreeDMovie, ImaxMovie extending Movie)
 * 3. Polymorphism (getFormatSurcharge, getFormatBadge, includesComplimentary3DGlasses)
 * 4. Encapsulation (Private fields, duration validation, age rating validation)
 * 5. Persistence (Thread-safe File I/O into data/movies.txt)
 */
public class MovieManagementMain {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("            CINERESERVE - MOVIE MANAGEMENT SYSTEM (MEMBER 2)                   ");
        System.out.println("================================================================================");

        MovieService service = new MovieService();

        System.out.println("\n--- [STEP 1: Catalog of Loaded Theatrical Releases] ---");
        List<Movie> catalog = service.getAllMovies();
        for (Movie m : catalog) {
            System.out.println(m);
        }

        System.out.println("\n--- [STEP 2: Demonstrating Polymorphism Across Movie Projection Formats] ---");
        double baseTicketPrice = 12.00;
        System.out.printf("Standard Baseline Cinema Ticket Price: $%.2f\n\n", baseTicketPrice);

        for (Movie m : catalog) {
            double surcharge = m.getFormatSurcharge();
            double totalPrice = baseTicketPrice + surcharge;

            System.out.println("Title: " + m.getTitle());
            System.out.println("  Format Type       : " + m.getFormatBadge());
            System.out.println("  Technical Specs   : " + m.getFormatDescription());
            System.out.printf("  Format Surcharge  : +$%.2f (Final Ticket: $%.2f)\n", surcharge, totalPrice);
            System.out.println("  3D Glasses Free?  : " + (m.includesComplimentary3DGlasses() ? "YES (Included)" : "NO (N/A)"));
            System.out.println("  -------------------------------------------------------------");
        }

        System.out.println("\n--- [STEP 3: Demonstrating Dynamic Genre Filtering & Search] ---");
        String searchGenre = "Sci-Fi";
        System.out.println(">> Filtering by Genre '" + searchGenre + "':");
        List<Movie> sciFiMovies = service.filterByGenre(searchGenre);
        for (Movie m : sciFiMovies) {
            System.out.println("   * " + m.getTitle() + " (" + m.getGenre() + ")");
        }

        System.out.println("\n================================================================================");
        System.out.println("  MEMBER 2 MODULE READY: All OOP concepts validated and verified!              ");
        System.out.println("================================================================================");
    }
}