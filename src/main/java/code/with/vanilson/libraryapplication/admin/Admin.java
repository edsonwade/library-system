package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.Member.Member;
import code.with.vanilson.libraryapplication.Person.Address;
import code.with.vanilson.libraryapplication.Person.Person;
import code.with.vanilson.libraryapplication.fine.Fine;
import code.with.vanilson.libraryapplication.librarian.Librarian;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends Person {
    @Column(name = "admin_code", unique = true, nullable = false)
    private String adminCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Librarian> managedLibrarians;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Member> managedMembers;

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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