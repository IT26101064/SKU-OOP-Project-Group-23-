package com.cinereserve.movie.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing an IMAX 70mm / Laser screening.
 */
public class ImaxMovie extends Movie {
    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_IMAX_SURCHARGE = 5.00;

    public ImaxMovie() {
        super();
        setFormatType("IMAX");
    }

    public ImaxMovie(String movieId, String title, String genre, String language,
                     int durationMinutes, String ageRating, String posterUrl,
                     String description, String releaseDate, String status) {
        super(movieId, title, genre, language, durationMinutes, ageRating, "IMAX", posterUrl, description, releaseDate, status);
    }

    @Override
    public double getFormatSurcharge() {
        return DEFAULT_IMAX_SURCHARGE; // $5.00 IMAX laser projection surcharge
    }

    @Override
    public String getFormatBadge() {
        return "IMAX LASER";
    }

    @Override
    public boolean includesComplimentary3DGlasses() {
        return false;
    }

    @Override
    public String getFormatDescription() {
        return "IMAX dual 4K laser projection with custom 12-channel tuned sound and floor-to-ceiling aspect ratio.";
    }
}