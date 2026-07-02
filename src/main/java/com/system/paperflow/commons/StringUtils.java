package com.system.paperflow.commons;

public final class StringUtils {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private StringUtils() {
    }

    public static boolean isValid(String value) {
        return !normalize(value).isEmpty();
    }

    public static boolean isEmail(String value) {
        return isValid(value) && normalize(value).matches(EMAIL_REGEX);
    }

    public static boolean hasMinLength(String value, int minLength) {
        value = normalize(value);
        return value.length() >= minLength;
    }

    public static boolean hasMaxLength(String value, int maxLength) {
        value = normalize(value);
        return !value.isEmpty() && value.length() <= maxLength;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}