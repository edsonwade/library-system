package code.with.vanilson.libraryapplication.util.http;

import code.with.vanilson.libraryapplication.book.BookResponse;
import code.with.vanilson.libraryapplication.common.https.HeaderConstants;
import org.springframework.http.HttpHeaders;

/**
 * HeaderUtil
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-03-21
 */
@SuppressWarnings("all")
public class HeaderUtil {
    public static HttpHeaders prepareBookResponseHeaders(BookResponse bookResponse, boolean includeCookie) {
        HttpHeaders headers = new HttpHeaders();

        if (bookResponse != null) {
            headers.set(HeaderConstants.X_BOOK_ID, String.valueOf(bookResponse.getId()));
            headers.set(HeaderConstants.X_BOOK_TITLE, bookResponse.getTitle());
            headers.set(HeaderConstants.X_BOOK_AUTHOR, bookResponse.getAuthor());
            headers.set(HeaderConstants.X_BOOK_ISBN, bookResponse.getIsbn());
        }
        return headers;
    }
}
