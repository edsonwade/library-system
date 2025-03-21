package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.fine.Fine;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import code.with.vanilson.libraryapplication.member.Member;
import code.with.vanilson.libraryapplication.person.Address;
import code.with.vanilson.libraryapplication.person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Admin
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@Entity
@Table(name = "admins")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Admin extends Person {

    @Column(name = "admin_code", unique = true, nullable = false)
    private String adminCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private Set<Librarian> managedLibrarians;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private Set<Member> managedMembers;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private Set<Fine> managedFines;

    // Constructor with fields
    public Admin(String name, String email, Address address, String contact, String adminCode, Role role) {
        super(name, email, address, contact);
        this.adminCode = adminCode;
        this.role = role;
    }

    public Admin(Long id, String name, String email, Address address, String contact, String adminCode, Role role) {
        super(id, name, email, address, contact);
        this.adminCode = adminCode;
        this.role = role;
    }

}