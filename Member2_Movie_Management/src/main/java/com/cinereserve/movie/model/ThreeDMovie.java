package com.cinereserve.movie.model;

/**
 * =========================================================================
 * OOP PRINCIPLE DEMONSTRATED: INHERITANCE & POLYMORPHISM
 * =========================================================================
 * Subclass representing a stereoscopic 3D film.
 */
public class ThreeDMovie extends Movie {
    private static final long serialVersionUID = 1L;

    public static final double DEFAULT_3D_SURCHARGE = 3.50;

    public ThreeDMovie() {
        super();
        setFormatType("3D");
    }

    public ThreeDMovie(String movieId, String title, String genre, String language,
                       int durationMinutes, String ageRating, String posterUrl,
                       String description, String releaseDate, String status) {
        super(movieId, title, genre, language, durationMinutes, ageRating, "3D", posterUrl, description, releaseDate, status);
    }

    @Override
    public double getFormatSurcharge() {
        return DEFAULT_3D_SURCHARGE; // $3.50 glasses and 3D projection fee
    }

    @Override
    public String getFormatBadge() {
        return "RealD 3D";
    }

    @Override
    public boolean includesComplimentary3DGlasses() {
        return true;
    }

    @Override
    public String getFormatDescription() {
        return "High-depth stereoscopic 3D presentation with sanitized complimentary circular polarized 3D glasses.";
    }
}