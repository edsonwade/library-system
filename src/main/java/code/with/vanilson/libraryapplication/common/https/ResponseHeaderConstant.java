package code.with.vanilson.libraryapplication.common.https;

/**
 * ResponseHeaderConstant
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@SuppressWarnings("unused")
public class ResponseHeaderConstant {
    // Common response header field names
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String LOCATION = "Location";
    public static final String ETAG = "ETag";
    public static final String SERVER = "Server";
    public static final String DATE = "Date";
    public static final String EXPIRES = "Expires";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LENGTH = "Content-Length";



    private ResponseHeaderConstant() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Utility class cannot be instantiated");
    }
}