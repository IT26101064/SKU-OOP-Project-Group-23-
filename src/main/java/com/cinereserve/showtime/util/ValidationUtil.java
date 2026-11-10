package com.cinereserve.showtime.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("^([01]?\\d|2[0-3]):[0-5]\\d$");

    private ValidationUtil() {}

    public static boolean isValidDate(String date) {
        return date != null && DATE_PATTERN.matcher(date.trim()).matches();
    }

    public static boolean isValidTime(String time) {
        return time != null && TIME_PATTERN.matcher(time.trim()).matches();
    }
}