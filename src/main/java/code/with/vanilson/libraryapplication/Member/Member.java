package code.with.vanilson.libraryapplication.Member;

import code.with.vanilson.libraryapplication.Person.Person;
import code.with.vanilson.libraryapplication.book.Book;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import jakarta.persistence.*;
import lombok.*;

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
@SequenceGenerator(name = "person_seq", sequenceName = "members_id_seq", allocationSize = 1)
public class Member extends Person {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus membershipStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "member_books",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> borrowedBooks;

    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian; // Librarian managing this member
}