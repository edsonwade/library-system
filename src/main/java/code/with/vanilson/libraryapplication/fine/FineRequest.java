package code.with.vanilson.libraryapplication.fine;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * FineRequest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineRequest {
    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be positive")
    private double amount;

    @Past(message = "Issue date must be in the past")
    private LocalDate issueDate;

    @FutureOrPresent(message = "Due date must be in the future or present")
    private LocalDate dueDate;

    @NotNull(message = "Due date must not be null")
    private Boolean isPaid;

    // Only include IDs or relevant fields instead of full objects
    @NotNull(message = "member must not be null")
    private Long memberId;

    @NotNull(message = "Librarian must not be null")
    private Long librarianId;

    @NotNull(message = "Admin must not be null")
    private Long adminId;
}