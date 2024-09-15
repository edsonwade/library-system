package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.member.MemberDTO;
import code.with.vanilson.libraryapplication.librarian.LibrarianDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

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
public class BookResponse extends RepresentationModel<BookResponse> {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String publisherName;
    private Integer publisherYear;
    private BookStatus status;
    private Set<MemberDTO> members; // Updated to Set<member>
    private LibrarianDTO librarian; // Updated to Librarian
}