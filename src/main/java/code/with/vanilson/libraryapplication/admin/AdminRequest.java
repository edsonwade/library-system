package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BookRequest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {
    @NotNull(message = "Name must not be null")
    @NotEmpty(message = "Name should not be empty")
    @NotBlank(message = "Name should not be blank")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @NotNull(message = "Contact must not be null")
    @Pattern(regexp = "^\\+\\d{1,3} \\d{3}-\\d{3}-\\d{3}$", message = "Contact must be in the format +XXX XXX-XXX-XXX")
    private String contact;

    @NotNull(message = "Admin code must not be null")
    @NotEmpty(message = "Admin code should not be empty")
    @Size(min = 1, max = 100, message = "Admin code must be between 1 and 100 characters")
    private String adminCode;

    @NotNull(message = "Role must not be null")
    private Role role;

}