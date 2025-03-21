package code.with.vanilson.libraryapplication.member;

import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.book.Book;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * member
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@EqualsAndHashCode(callSuper = false)
@Table(name = "members")
@Entity(name = "Member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Person {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus membershipStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "member_books",
            joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    @JsonIgnore
    private Set<Book> borrowedBooks = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "librarian_id")
    @JsonIgnore
    private Librarian librarian; // Librarian managing this member

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "admin_id") // Foreign key to Admin
    private Admin admin; // Admin managing this member

    public Member(String name, String email,
                  Address address, String contact,
                  MembershipStatus membershipStatus, Librarian librarian, Admin admin) {
        super(name, email, address, contact);
        this.membershipStatus = membershipStatus;
        this.librarian = librarian;
        this.admin = admin;
    }
}