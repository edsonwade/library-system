package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.book.BookResponse;
import code.with.vanilson.libraryapplication.librarian.LibrarianResponse;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * MemberDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class MemberResponse extends RepresentationModel<MemberResponse> {
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    private AddressDTO address;
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Contact number is invalid")
    @NotBlank(message = "Contact is mandatory")
    private String contact;

    private MembershipStatus membershipStatus;

    private BookResponse bookResponse;

    private LibrarianResponse librarianResponse;

    private AdminResponse adminResponse;
}
