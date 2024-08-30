package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Member.MembershipStatus;
import code.with.vanilson.libraryapplication.Person.AddressDTO;
import code.with.vanilson.libraryapplication.book.BookStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * BookRequest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Data
@Builder
public class AdminRequest {
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
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be a 10-digit number")
    private String contact;

    @NotNull(message = "Admin code must not be null")
    @NotEmpty(message = "Admin code should not be empty")
    @NotBlank(message = "Admin code should not be blank")
    @Size(min = 1, max = 100, message = "Admin code must be between 1 and 100 characters")
    private String adminCode;

    @NotNull(message = "Role must not be null")
    private Role role;

}