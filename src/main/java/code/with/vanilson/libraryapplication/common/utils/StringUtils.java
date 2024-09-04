package code.with.vanilson.libraryapplication.common.utils;

/**
 * StringUtils
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */
@SuppressWarnings("unused")
public class StringUtils {

    // Checks if the string is null or empty
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    // Checks if the string is not null and not empty
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    // Checks if the string is null or consists only of whitespace
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Checks if the string is not null and not only whitespace
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    // Returns an empty string if the input is null
    public static String defaultString(String str) {
        return str == null ? "" : str;
    }

    // Returns a default string if the input is null or empty
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    // Removes leading and trailing whitespace
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    // Trims and converts to null if the result is empty
    public static String trimToNull(String str) {
        String trimmed = trim(str);
        return isEmpty(trimmed) ? null : trimmed;
    }

    // Trims and converts to empty string if the result is empty
    public static String trimToEmpty(String str) {
        String trimmed = trim(str);
        return trimmed != null ? trimmed : "";
    }

    // Returns a substring from the start index to the end
    public static String substring(String str, int start) {
        if (str == null || start > str.length()) {
            return null;
        }
        return str.substring(start);
    }

    // Returns a substring from the start index to the end index
    public static String substring(String str, int start, int end) {
        if (str == null || start > end || start > str.length() || end > str.length()) {
            return null;
        }
        return str.substring(start, end);
    }

    // Finds the index of the first occurrence of a substring
    public static int indexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.indexOf(searchStr);
    }

    // Finds the index of the last occurrence of a substring
    public static int lastIndexOf(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return -1;
        }
        return str.lastIndexOf(searchStr);
    }

    // Replaces all occurrences of a substring with a new substring
    public static String replace(String text, String searchString, String replacement) {
        if (text == null || searchString == null || replacement == null) {
            return text;
        }
        return text.replace(searchString, replacement);
    }

    // Joins elements of a collection into a single string with the specified separator
    public static String join(Object[] elements, String separator) {
        if (elements == null || separator == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(elements[i]);
        }
        return sb.toString();
    }

    // Abbreviates a string to a specified width
    public static String abbreviate(String str, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        return str.substring(0, maxWidth - 3) + "...";
    }

    // Converts the string to uppercase
    public static String upperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    // Converts the string to lowercase
    public static String lowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    // Capitalizes the first letter of the string
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Capitalizes the first letter of the string
    public static String uncapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    // Pads the string on the left to a specified length
    public static String leftPad(String str, int size) {
        if (str == null) {
            return null;
        }
        if (str.length() >= size) {
            return str;
        }
        return String.format("%d %s", size, str);
    }

    // Pads the string on the right to a specified length
    public static String rightPad(String str, int size) {
        if (str == null) {
            return null;
        }
        if (str.length() >= size) {
            return str;
        }
        return String.format("%d and %s", size, str);
    }

    // Centers the string within a specified length
    // Centers the string within a specified length
    public static String center(String str, int size) {
        if (str == null) {
            return null;
        }
        if (str.length() >= size) {
            return str;
        }
        int padding = size - str.length();
        int padLeft = padding / 2;
        int padRight = padding - padLeft;
        return String.format("%d  %s %s %d ", padLeft, str.length(), str, padRight);
    }

    // Checks if a string contains a sequence
    public static boolean contains(String str, CharSequence sequence) {
        if (str == null || sequence == null) {
            return false;
        }
        return str.contains(sequence);
    }

    private StringUtils() {
        throw new AssertionError("No instances allowed");
    }
}
