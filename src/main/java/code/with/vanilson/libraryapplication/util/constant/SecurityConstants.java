package code.with.vanilson.libraryapplication.util.constant;

/**
 * This class contains constants related to security and authentication.
 */
public class SecurityConstants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ISSUER = "auth-api";
    public static final long EXPIRATION_TIME_IN_HOURS = 2;

    // Private constructor to prevent instantiation
    private SecurityConstants() {}
}