package code.with.vanilson.libraryapplication.book;

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
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBookByAuthorAndTitle(String author, String title);

    public boolean existsBooksByTitle(String title);

    public boolean existsBooksByAuthorAndTitle(String author, String title);
}
