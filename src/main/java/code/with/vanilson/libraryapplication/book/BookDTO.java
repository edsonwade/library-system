package code.with.vanilson.libraryapplication.book;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * BookDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    @NotNull(message = "The book title must not be null")
    @NotEmpty(message = "The book title should not be empty")
    @NotBlank(message = "The book title should not be blank")
    @Size(min = 1, max = 255, message = "The book title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "The book author must not be null")
    @NotEmpty(message = "The book author should not be empty")
    @NotBlank(message = "The book author should not be blank")
    @Size(min = 1, max = 255, message = "The book author must be between 1 and 255 characters")
    private String author;

    @Pattern(regexp = "^(97[89]\\d{9}[\\dX]|\\d{9}[\\dX])$",
            message = "The ISBN must be a valid ISBN-10 or ISBN-13 format")
    private String isbn;

    @Size(max = 100, message = "The genre must be no more than 100 characters long")
    private String genre;

    @Size(max = 100, message = "The publisher name must be no more than 100 characters long")
    private String publisherName;

    @Positive(message = "The publisher year must be a positive integer")
    private Integer publisherYear;

    @NotNull(message = "The book status must not be null")
    private BookStatus status;

    @Size(min = 1, message = "There must be at least one member ID")
    private Set<@Positive(message = "Each member ID must be a positive integer") Long> memberIds;

    @NotNull(message = "The librarian must not be null")
    @Positive(message = "The librarian ID must be a positive integer")
    @Min(value = 1, message = "The librarian ID must be greater than or equal to 1")
    private Long librarianId;

}
