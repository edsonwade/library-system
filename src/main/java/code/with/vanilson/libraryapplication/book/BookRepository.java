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

    Optional<Book> findBookByAuthor(String author);

    Optional<Book> findBookByIsbn(String isbn);

    Optional<Book> findBookByTitle(String title);

    Optional<Book> findBookByGenre(String genre);

    public boolean existsBooksByIsbn(String isbn);

}
