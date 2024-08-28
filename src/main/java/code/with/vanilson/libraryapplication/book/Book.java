package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Book
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@Table(name = "books")
@Entity(name = "Book")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(value = {"id", "title", "author", "isbn", "publisherName", "publisherYear", "status"})
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @Column(name = "book_id", nullable = false, unique = true)
    private Long id;  // Use Long to match BIGINT

    private String title;
    private String author;
    private String isbn;
    private String genre;

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "publisher_year")
    private Integer publisherYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus status;

    @ManyToMany(mappedBy = "borrowedBooks", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Member> members;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "librarian_id")
    private Librarian librarian; // Librarian managing this book

    /**
     * Adds a member to the set of members who have borrowed this book.
     * Also adds this book to the member's list of borrowed books.
     *
     * @param member The member to be added to the set of borrowers.
     * @return The updated book instance with the added member.
     */
    public Book addBookMember(Member member) {
        if (null == members) {
            members = new HashSet<>();
        }
        members.add(member);
        member.getBorrowedBooks().add(this);
        return this;
    }

    // Method to check if the book is reserved by the given member

    /**
     * Checks if the book is reserved by the given member.
     *
     * @param member The member to check if they have reserved this book.
     * @return {@code true} if the book is reserved by the given member, {@code false} otherwise.
     * The book is considered reserved by a member if the member is present in the set of members
     * who have borrowed this book.
     */
    public boolean isReservedBy(Member member) {
        return members != null && members.contains(member);
    }

    /**
     * Checks if the book is available for borrowing.
     *
     * @return {@code true} if the book is available, {@code false} otherwise.
     * The book is considered available if its status is {@link BookStatus#AVAILABLE}.
     */
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    /**
     * Checks if the book is either borrowed or loaned.
     *
     * @return {@code true} if the book is either borrowed or loaned, {@code false} otherwise.
     * The book is considered borrowed or loaned if its status is either
     */
    public boolean isBorrowedOrLoaned() {
        return isBorrowed() || isLoaned();
    }

    /**
     * Checks if the book is currently borrowed.
     *
     * @return {@code true} if the book is borrowed, {@code false} otherwise.
     * The book is considered borrowed if its status is {@link BookStatus#BORROWED}.
     */
    public boolean isBorrowed() {
        return status == BookStatus.BORROWED;
    }

    /**
     * Checks if the book is currently loaned.
     *
     * @return {@code true} if the book is loaned, {@code false} otherwise.
     * The book is considered loaned if its status is {@link BookStatus#LOANED}.
     */
    public boolean isLoaned() {
        return status == BookStatus.LOANED;
    }

    /**
     * Checks if the book is currently lost.
     *
     * @return {@code true} if the book is lost, {@code false} otherwise.
     * The book is considered lost if its status is {@link BookStatus#LOST}.
     */

    public boolean isLost() {
        return status == BookStatus.LOST;
    }

    /**
     * Checks if the book is currently reserved.
     *
     * @return {@code true} if the book is reserved, {@code false} otherwise.
     * The book is considered reserved if its status is {@link BookStatus#RESERVED}.
     */

    public boolean isReserved() {
        return status == BookStatus.RESERVED;
    }

}