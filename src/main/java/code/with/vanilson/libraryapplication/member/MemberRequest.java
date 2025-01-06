package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class MemberRequest {
    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name should not be empty")
    @NotBlank(message = "Name should not be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @NotNull(message = "Contact must not be null")
    @Pattern(regexp = "^\\+\\d{1,3} \\d{3}-\\d{3}-\\d{3}$", message = "Contact must be in the format +XXX XXX-XXX-XXX")
    private String contact;

    @NotNull(message = "Membership status must not be null")
    private MembershipStatus membershipStatus;

    // Use IDs instead of full objects to avoid recursion issues
    @NotNull(message = "Librarian ID must not be null")
    private Long librarianId;

    @NotNull(message = "Admin ID must not be null")
    private Long adminId;


}
