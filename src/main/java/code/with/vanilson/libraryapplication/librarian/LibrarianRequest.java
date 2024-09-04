package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.Person.AddressDTO;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

/**
 * LibrarianRequest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Data
@Builder
public class LibrarianRequest {
    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be no longer than 255 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 255, message = "Email must be no longer than 255 characters")
    private String email;

    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @NotNull(message = "Contact must not be null")
    @NotBlank(message = "Contact must not be blank")
    @Pattern(regexp = "^\\+\\d{1,3} \\d{3}-\\d{3}-\\d{3}$", message = "Contact must be in the format +XXX XXX-XXX-XXX")
    private String contact;

    @NotNull(message = "Employee code must not be null")
    @NotBlank(message = "Employee code must not be blank")
    @Size(max = 50, message = "Employee code must be no longer than 50 characters")
    private String employeeCode;

    @NotNull(message = "Librarian must not be null")
    private Long admin;
}
