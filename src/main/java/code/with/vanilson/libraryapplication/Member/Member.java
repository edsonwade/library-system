package code.with.vanilson.libraryapplication.Member;

import code.with.vanilson.libraryapplication.Person.Person;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.book.Book;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Member
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@Table(name = "members")
@Entity(name = "Member")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus membershipStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "member_books",
            joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @JsonIgnore
    private Set<Book> borrowedBooks;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    @JsonIgnore
    private Librarian librarian; // Librarian managing this member

    @ManyToOne
    @JoinColumn(name = "admin_id") // Foreign key to Admin
    private Admin admin; // Admin managing this member

}