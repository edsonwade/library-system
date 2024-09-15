package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.book.BookResponse;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * MemberDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse extends RepresentationModel<MemberResponse> {
    private Long id;

    private String name;

    private String email;

    private AddressDTO address;

    private String contact;

    private MembershipStatus membershipStatus;

    private BookResponse bookResponse;

    private LibrarianResponse librarianResponse;

    private AdminResponse adminResponse;
}
