package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Person.Person;
import code.with.vanilson.libraryapplication.admin.Admin;
import code.with.vanilson.libraryapplication.book.Book;
import code.with.vanilson.libraryapplication.fine.Fine;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
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
public class Librarian extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Book> managedBooks;

    @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Member> members;

    @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Fine> fines;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id") // Foreign key to Admin
    @JsonIgnore
    private Admin admin; // Admin managing this fine
}