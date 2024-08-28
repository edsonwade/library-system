package code.with.vanilson.libraryapplication.fine;

import code.with.vanilson.libraryapplication.Member.MemberDTO;
import code.with.vanilson.libraryapplication.admin.AdminDTO;
import code.with.vanilson.libraryapplication.librarian.LibrarianDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * FineDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineDTO {
    @NotNull(message = "ID must not be null")
    private Long id;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull(message = "Issue date must not be null")
    @Past(message = "Issue date must be in the past")
    private Date issueDate;

    @NotNull(message = "Due date must not be null")
    @FutureOrPresent(message = "Due date must be in the future or present")
    private Date dueDate;

    // Only include IDs or relevant fields instead of full objects
    @NotNull(message = "Member must not be null")
    private Long memberId;

    @NotNull(message = "Librarian must not be null")
    private Long librarianId;

    @NotNull(message = "Admin must not be null")
    private Long adminId;
}
