package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * BookResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Data
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String publisherName;
    private Integer publisherYear;
    private BookStatus status;
    private Long members; // Full Member objects
    private Long librarian; // Full Librarian object
}