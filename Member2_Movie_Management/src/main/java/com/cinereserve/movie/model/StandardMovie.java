package com.cinereserve.movie.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing a standard 2D theatrical release.
 */
public class StandardMovie extends Movie {
    private static final long serialVersionUID = 1L;

    public StandardMovie() {
        super();
        setFormatType("STANDARD");
    }

    public StandardMovie(String movieId, String title, String genre, String language,
                         int durationMinutes, String ageRating, String posterUrl,
                         String description, String releaseDate, String status) {
        super(movieId, title, genre, language, durationMinutes, ageRating, "STANDARD", posterUrl, description, releaseDate, status);
    }

    @Override
    public double getFormatSurcharge() {
        return 0.00; // Standard 2D has no format surcharge
    }

    @Override
    public String getFormatBadge() {
        return "STANDARD 2D";
    }

    @Override
    public boolean includesComplimentary3DGlasses() {
        return false;
    }

    @Override
    public String getFormatDescription() {
        return "Standard 2D digital cinema projection at 24fps with Dolby 7.1 Surround Sound.";
    }
}