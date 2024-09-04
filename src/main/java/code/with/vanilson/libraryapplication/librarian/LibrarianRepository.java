package code.with.vanilson.libraryapplication.librarian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * BookRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
    boolean existsLibrarianByEmail(String email);

    boolean existsLibrarianByEmployeeCode(String code);

    boolean existsLibrarianByContact(String contact);

    Optional<Librarian> findLibrariansByEmail(String email);

    boolean existsLibrarianByEmailAndIdNot(String email, Long librarianId);

    boolean existsLibrarianByContactAndIdNot(String contact, Long librarianId);

    boolean existsLibrarianByEmployeeCodeAndIdNot(String employeeCode, Long librarianId);
}
