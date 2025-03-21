package code.with.vanilson.libraryapplication.util.cookies;

import code.with.vanilson.libraryapplication.common.https.HeaderConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

/**
 * CookieUtil
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-03-21
 */
@SuppressWarnings("all")
public class CookieUtil {
    public static void addSessionCookieIfRequired(boolean includeCookie, HttpHeaders headers) {
        if (includeCookie) {
            ResponseCookie sessionCookie = ResponseCookie.from("sessionId", "abc123")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Strict")
                    .build();
            headers.add(HeaderConstants.SET_COOKIE, sessionCookie.toString());
        }
    }

}
