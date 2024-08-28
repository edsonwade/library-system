package code.with.vanilson.libraryapplication.common.constants;

/**
 * ErrorCodes
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
public class ErrorCodes {
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String CONFLICT = "CONFLICT";
    public static final String UNPROCESSABLE_ENTITY = "UNPROCESSABLE_ENTITY";
    public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    public static final String UNEXPECTED_ERROR = "An unexpected error occurred";

    private ErrorCodes() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Utility class cannot be instantiated");
    }
}