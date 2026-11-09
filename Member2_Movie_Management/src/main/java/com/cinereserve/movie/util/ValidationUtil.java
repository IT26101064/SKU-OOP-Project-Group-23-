package com.cinereserve.movie.util;

public final class ValidationUtil {
    private ValidationUtil() {}

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidDuration(int duration) {
        return duration >= 30 && duration <= 360;
    }

    public static boolean isValidAgeRating(String rating) {
        if (rating == null) return false;
        String r = rating.trim().toUpperCase();
        return r.equals("G") || r.equals("PG") || r.equals("PG-13") || r.equals("R") || r.equals("NC-17");
    }
}