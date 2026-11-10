package com.cinereserve.movie.model;

import java.io.Serializable;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: ABSTRACTION & ENCAPSULATION
 * =========================================================================
 * Abstract base class representing a Movie in the theater catalog.
 * Defines the contract that every projection format (2D, 3D, IMAX) must implement.
 */
public abstract class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String STATUS_NOW_SHOWING = "NOW_SHOWING";
    public static final String STATUS_UPCOMING = "UPCOMING";
    public static final String STATUS_ARCHIVED = "ARCHIVED";

    // Encapsulated private attributes
    private String movieId;
    private String title;
    private String genre;
    private String language;
    private int durationMinutes;
    private String ageRating; // "G", "PG", "PG-13", "R", "NC-17"
    private String formatType; // "STANDARD", "3D", "IMAX"
    private String posterUrl;
    private String description;
    private String releaseDate;
    private String status; // NOW_SHOWING, UPCOMING, ARCHIVED

    public Movie() {}

    public Movie(String movieId, String title, String genre, String language,
                 int durationMinutes, String ageRating, String formatType,
                 String posterUrl, String description, String releaseDate, String status) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
        this.formatType = formatType;
        this.posterUrl = posterUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.status = status != null ? status : STATUS_NOW_SHOWING;
    }

    // =========================================================================
    // ABSTRACT METHODS (ABSTRACTION & POLYMORPHISM)
    // =========================================================================

    /**
     * Calculates base ticket price surcharge according to format.
     */
    public abstract double getFormatSurcharge();

    /**
     * Returns a formatted badge for UI/Console.
     */
    public abstract String getFormatBadge();

    /**
     * Checks if 3D glasses are supplied.
     */
    public abstract boolean includesComplimentary3DGlasses();

    /**
     * Technical specs and description.
     */
    public abstract String getFormatDescription();

    // =========================================================================
    // ENCAPSULATED GETTERS & SETTERS
    // =========================================================================

    public boolean isNowShowing() {
        return STATUS_NOW_SHOWING.equalsIgnoreCase(this.status);
    }

    public boolean isUpcoming() {
        return STATUS_UPCOMING.equalsIgnoreCase(this.status);
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }

    public String getFormatType() { return formatType; }
    public void setFormatType(String formatType) { this.formatType = formatType; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Title: '%s' | Genre: %s | Rating: %s | Duration: %d min | Status: %s",
                getFormatBadge(), movieId, title, genre, ageRating, durationMinutes, status);
    }
}