package com.cinereserve.user.util;

/**
 * Safe delimiter escaping utility for text-file persistence.
 */
public final class StringUtil {
    private static final String DELIMITER = "|";
    private static final String ESCAPED_DELIMITER = "\\pipe;";
    private static final String NEWLINE_CHAR = "\n";
    private static final String ESCAPED_NEWLINE = "\\newline;";
    private static final String RETURN_CHAR = "\r";

    private StringUtil() {}

    public static String escape(String input) {
        if (input == null) return "";
        return input.replace(DELIMITER, ESCAPED_DELIMITER)
                    .replace(RETURN_CHAR, "")
                    .replace(NEWLINE_CHAR, ESCAPED_NEWLINE)
                    .trim();
    }

    public static String unescape(String input) {
        if (input == null) return "";
        return input.replace(ESCAPED_DELIMITER, DELIMITER)
                    .replace(ESCAPED_NEWLINE, NEWLINE_CHAR);
    }

    public static String[] splitRecord(String line) {
        if (line == null) return new String[0];
        String[] parts = line.split("\\|", -1);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = unescape(parts[i]);
        }
        return parts;
    }

    public static String joinRecord(String... fields) {
        if (fields == null || fields.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append("|");
            sb.append(escape(fields[i]));
        }
        return sb.toString();
    }
}
