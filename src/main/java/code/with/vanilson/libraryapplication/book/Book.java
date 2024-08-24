package code.with.vanilson.libraryapplication.book;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@Table(name = "books")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"id", "title", "author", "isbn", "publisherName", "publisherYear", "status"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @Column(name = "book_id", nullable = false, unique = true)
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    @Column(name = "publisher_name")
    private String publisherName;
    @Column(name = "publisher_year")
    private Integer publisherYear;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "book_status")
    private BookStatus status;

}