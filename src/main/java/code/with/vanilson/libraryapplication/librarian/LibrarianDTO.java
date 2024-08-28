package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.Member.MemberDTO;
import code.with.vanilson.libraryapplication.Person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.AdminDTO;
import code.with.vanilson.libraryapplication.book.BookDTO;
import code.with.vanilson.libraryapplication.fine.FineDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * MemberDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibrarianDTO {
    @NotNull(message = "ID must not be null")
    private Long id;

    @NotNull(message = "Name must not be null")
    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be no longer than 255 characters")
    private String name;

    @NotNull(message = "Email must not be null")
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be no longer than 255 characters")
    private String email;

    @NotNull(message = "Address must not be null")
    private AddressDTO address;

    @NotNull(message = "Contact must not be null")
    @NotBlank(message = "Contact must not be blank")
    @Size(max = 20, message = "Contact must be no longer than 20 characters")
    private String contact;

    @NotNull(message = "Employee code must not be null")
    @NotBlank(message = "Employee code must not be blank")
    @Size(max = 50, message = "Employee code must be no longer than 50 characters")
    private String employeeCode;

    // Avoid including `managedBooks` or `members` here to prevent recursion
    private Set<Long> managedBooksIds;

    private Set<Long> membersIds;

    @NotNull(message = "Admin must not be null")
    private AdminDTO admin;
}
