package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Person.Person;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.book.Book;
import code.with.vanilson.libraryapplication.fine.Fine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Librarian
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@Entity(name = "Librarian")
@Table(name = "librarians")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "person_seq", sequenceName = "librarians_id_seq", allocationSize = 1)
public class Librarian extends Person {

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @OneToMany(mappedBy = "librarian")
    private Set<Book> managedBooks;

    @OneToMany(mappedBy = "librarian")
    private Set<Member> members;

    @OneToMany(mappedBy = "librarian")
    private Set<Fine> fines;

    @ManyToOne
    @JoinColumn(name = "admin_id") // Foreign key to Admin
    private Admin admin; // Admin managing this fine

}