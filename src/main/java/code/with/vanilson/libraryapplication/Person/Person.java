package code.with.vanilson.libraryapplication.Person;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Member
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@MappedSuperclass // inherited from Member constructor methods
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email is not valid",
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Column(nullable = false)
    @Embedded
    private Address address;

    @Column(nullable = false, unique = true)
    private String contact;

    // Constructor with fields
    protected Person(String name, String email, Address address, String contact) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
    }

}