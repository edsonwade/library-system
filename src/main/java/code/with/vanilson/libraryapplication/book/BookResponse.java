package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.member.MemberResponse;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

/**
 * BookResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
public class BookResponse extends RepresentationModel<BookResponse> {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String publisherName;
    private Integer publisherYear;
    private BookStatus status;
    private Set<MemberResponse> members; // Updated to Set<member>
    private LibrarianResponse librarian; // Updated to Librarian
}