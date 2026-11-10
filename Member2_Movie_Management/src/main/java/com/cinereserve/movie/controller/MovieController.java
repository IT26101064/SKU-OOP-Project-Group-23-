package com.cinereserve.movie.controller;

import com.cinereserve.movie.model.Movie;
import com.cinereserve.movie.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * MEMBER 2 - MOVIE MANAGEMENT CONTROLLER (SE1020 OOP PROJECT)
 * ============================================================================
 * Web endpoints managing:
 * 1. Add New Movie (Create Form) -> /movies/add
 * 2. Movie Catalog & Now Showing Filter (Read) -> /movies
 * 3. Movie Details & Status Update (Update) -> /movies/edit/{id}
 * 4. Movie Deletion (Delete) -> /movies/delete/{id}
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService = new MovieService();

    // UI 2: Cinema Movie Catalog / Cards & Filters (Read)
    @GetMapping
    public String viewCatalog(@RequestParam(value = "genre", required = false) String genre,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "nowShowing", required = false) Boolean nowShowing,
                              Model model) {
        List<Movie> movies;
        if (nowShowing != null && nowShowing) {
            movies = movieService.getNowShowingMovies();
        } else if (genre != null && !genre.trim().isEmpty() && !"ALL".equalsIgnoreCase(genre)) {
            movies = movieService.filterByGenre(genre);
        } else if (search != null && !search.trim().isEmpty()) {
            movies = movieService.searchByTitle(search);
        } else {
            movies = movieService.getAllMovies();
        }

        model.addAttribute("movies", movies);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("searchQuery", search);
        model.addAttribute("nowShowingOnly", nowShowing);
        return "movie/catalog";
    }

    // UI 1: Add New Movie Page (Create Form)
    @GetMapping("/add")
    public String showAddMovieForm(Model model) {
        return "movie/add-movie";
    }

    // Process Add Movie Form
    @PostMapping("/add")
    public String addMovie(@RequestParam("title") String title,
                           @RequestParam("genre") String genre,
                           @RequestParam("language") String language,
                           @RequestParam("duration") int duration,
                           @RequestParam("ageRating") String ageRating,
                           @RequestParam("formatType") String formatType,
                           @RequestParam(value = "posterUrl", required = false) String posterUrl,
                           @RequestParam(value = "description", required = false) String description,
                           @RequestParam("releaseDate") String releaseDate,
                           @RequestParam(value = "status", defaultValue = "NOW_SHOWING") String status,
                           RedirectAttributes redirectAttributes) {
        try {
            if (posterUrl == null || posterUrl.trim().isEmpty()) {
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=500&q=80";
            }
            Movie movie = movieService.addMovie(title, genre, language, duration, ageRating, formatType, posterUrl, description, releaseDate, status);
            redirectAttributes.addFlashAttribute("successMessage", "Movie '" + movie.getTitle() + "' added successfully! (" + movie.getFormatType() + ")");
            return "redirect:/movies";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/movies/add";
        }
    }

    // UI 3: Edit Movie Status & Details (Update)
    @GetMapping("/edit/{id}")
    public String showEditMovieForm(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Movie> movieOpt = movieService.getMovieById(id);
        if (movieOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Movie not found: " + id);
            return "redirect:/movies";
        }
        model.addAttribute("movie", movieOpt.get());
        return "movie/edit-movie";
    }

    // Update Movie Status
    @PostMapping("/edit/{id}")
    public String updateMovieStatus(@PathVariable("id") String id,
                                   @RequestParam("status") String status,
                                   RedirectAttributes redirectAttributes) {
        boolean updated = movieService.updateStatus(id, status);
        if (updated) {
            redirectAttributes.addFlashAttribute("successMessage", "Movie status updated to " + status);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update movie status.");
        }
        return "redirect:/movies";
    }

    // Delete Movie
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        boolean deleted = movieService.deleteMovie(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Movie deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete movie.");
        }
        return "redirect:/movies";
    }
}
