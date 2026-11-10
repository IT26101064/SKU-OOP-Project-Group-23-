package com.cinereserve.movie.service;

import com.cinereserve.movie.model.*;
import com.cinereserve.movie.repository.MovieRepository;
import com.cinereserve.movie.util.ValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieRepository repository;

    public MovieService() {
        this.repository = new MovieRepository();
    }

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public Movie addMovie(String title, String genre, String language, int duration,
                          String ageRating, String formatType, String posterUrl,
                          String description, String releaseDate, String status) {
        if (!ValidationUtil.isNotEmpty(title)) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (!ValidationUtil.isValidDuration(duration)) {
            throw new IllegalArgumentException("Duration must be between 30 and 360 minutes");
        }
        if (!ValidationUtil.isValidAgeRating(ageRating)) {
            throw new IllegalArgumentException("Invalid age rating (must be G, PG, PG-13, R, or NC-17)");
        }

        String movieId = "MOV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Movie movie;
        if ("3D".equalsIgnoreCase(formatType)) {
            movie = new ThreeDMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
        } else if ("IMAX".equalsIgnoreCase(formatType)) {
            movie = new ImaxMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
        } else {
            movie = new StandardMovie(movieId, title, genre, language, duration, ageRating, posterUrl, description, releaseDate, status);
        }

        repository.save(movie);
        return movie;
    }

    public List<Movie> getAllMovies() {
        return repository.findAll();
    }

    public Optional<Movie> getMovieById(String id) {
        return repository.findById(id);
    }

    public List<Movie> getNowShowingMovies() {
        return repository.findAll().stream().filter(Movie::isNowShowing).collect(Collectors.toList());
    }

    public List<Movie> filterByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) return getAllMovies();
        return repository.findAll().stream()
                .filter(m -> m.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Movie> searchByTitle(String query) {
        if (query == null || query.trim().isEmpty()) return getAllMovies();
        return repository.findAll().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean updateStatus(String movieId, String newStatus) {
        Optional<Movie> opt = repository.findById(movieId);
        if (!opt.isPresent()) return false;
        Movie movie = opt.get();
        movie.setStatus(newStatus);
        return repository.update(movie);
    }

    public boolean deleteMovie(String movieId) {
        return repository.deleteById(movieId);
    }
}