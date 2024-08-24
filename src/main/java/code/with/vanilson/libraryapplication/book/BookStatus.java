package code.with.vanilson.libraryapplication.book;

import lombok.Getter;

/**
 * BookStatus
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@Getter
public enum BookStatus {
    AVAILABLE("Available"),
    BORROWED("borrowed"),
    LOANED("loaned"),
    LOST("lost"),
    RESERVED("Reserved");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }
}