package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Embedded;

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
public class BookDTO {
    @NotNull(message = "The book ID must not be null")
    @Positive(message = "The book ID must be a positive integer")
    private Long id;

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

    @NotNull(message = "The librarian ID must not be null")
    @Positive(message = "The librarian ID must be a positive integer")
    private Long librarianId;

    @NotEmpty(message = "The member IDs should not be empty")
    @NotNull(message = "Member IDs must not be null")
    @NotEmpty(message = "Member IDs should not be empty")
    @Positive(message = "Member ID must be a positive integer")
    private Set<Long> memberIds;
}