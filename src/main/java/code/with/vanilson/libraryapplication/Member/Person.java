package code.with.vanilson.libraryapplication.Member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Member
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "person_id_seq", allocationSize = 1)
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

}