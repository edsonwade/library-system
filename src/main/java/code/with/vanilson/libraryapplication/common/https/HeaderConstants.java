package code.with.vanilson.libraryapplication.common.https;

/**
 * HeaderConstants
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
public class HeaderConstants {

    // Accept headers
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    // Access Control headers
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    // Content headers
    public static final String CONTENT_TYPE = "content-type";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String COOKIE = "Cookie";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LOCATION = "Location";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE_MAX_AGE = "Set-Cookie-Max-Age";
    public static final String SET_COOKIE_PATH = "Set-Cookie-Path";
    public static final String SET_COOKIE_SECURE = "Set-Cookie-Secure";
    public static final String SET_COOKIE_DOMAIN = "Set-Cookie-Domain";

    // Content type values
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String CONTENT_TYPE_CSV = "text/csv";
    public static final String CONTENT_TYPE_HTML = "text/html";



    // Security headers
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    public static final String X_XSS_PROTECTION = "X-XSS-Protection";

    // CORS header

    // Custom headers
    public static final String X_ADMIN_ID = "X-Admin-ID";
    public static final String X_ADMIN_NAME = "X-Admin-Name";
    public static final String X_ADMIN_EMAIL = "X-Admin-Email";
    public static final String X_ADMIN_CODE = "X-Admin-Code";


    // Common values
    public static final String STRICT_TRANSPORT_SECURITY_VALUE = "max-age=31536000; includeSubDomains";
    public static final String X_CONTENT_TYPE_OPTIONS_VALUE = "nosniff";
    public static final String X_FRAME_OPTIONS_VALUE = "DENY";
    public static final String X_XSS_PROTECTION_VALUE = "1; mode=block";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";


    private HeaderConstants() {
        // Private constructor to prevent instantiation
        throw new AssertionError("Utility class cannot be instantiated");
    }
}