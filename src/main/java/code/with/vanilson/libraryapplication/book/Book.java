package code.with.vanilson.libraryapplication.book;

import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

import static code.with.vanilson.libraryapplication.book.BookStatus.*;

/**
 * Book
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@SuppressWarnings("unused")
@Table(name = "books")
@Entity(name = "Book")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Getter
@lombok.Setter
@lombok.ToString(exclude = {"members", "librarian"})
@lombok.EqualsAndHashCode(exclude = {"members", "librarian"})
@JsonPropertyOrder(value = {"id", "title", "author", "isbn", "publisherName", "publisherYear", "status"})
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false, unique = true)
    private Long bookId;
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

    @ManyToMany(mappedBy = "borrowedBooks", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @Builder.Default
    private Set<Member> members = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "librarian_id")
    private Librarian librarian; // Librarian managing this book

    public Book(String title, String author, String isbn, String genre, String publisherName, Integer publisherYear,
                BookStatus status, Set<Member> members, Librarian librarian) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.publisherName = publisherName;
        this.publisherYear = publisherYear;
        this.status = status;
        this.members = members;
        this.librarian = librarian;
    }

    /**
     * Adds a member to the set of member who have borrowed this book.
     * Also adds this book to the member's list of borrowed books.
     *
     * @param member The member to be added to the set of borrowers.
     */
    public void addBookMember(Member member) {
        if (null == members) {
            members = new HashSet<>();
        }
        members.add(member);
        member.getBorrowedBooks().add(this);
    }

    // Method to check if the book is reserved by the given member

    /**
     * Checks if the book is reserved by the given member.
     *
     * @param member The member to check if they have reserved this book.
     * @return {@code true} if the book is reserved by the given member, {@code false} otherwise.
     * The book is considered reserved by a member if the member is present in the set of member
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
        return AVAILABLE.equals(status);
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
        return BORROWED.equals(status);
    }

    /**
     * Checks if the book is currently loaned.
     *
     * @return {@code true} if the book is loaned, {@code false} otherwise.
     * The book is considered loaned if its status is {@link BookStatus#LOANED}.
     */
    public boolean isLoaned() {
        return LOANED.equals(status);
    }

    /**
     * Checks if the book is currently lost.
     *
     * @return {@code true} if the book is lost, {@code false} otherwise.
     * The book is considered lost if its status is {@link BookStatus#LOST}.
     */

    public boolean isLost() {
        return LOST.equals(status);
    }

    /**
     * Checks if the book is currently reserved.
     *
     * @return {@code true} if the book is reserved, {@code false} otherwise.
     * The book is considered reserved if its status is {@link BookStatus#RESERVED}.
     */

    public boolean isReserved() {
        return RESERVED.equals(status);
    }

}
