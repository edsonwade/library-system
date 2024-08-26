package code.with.vanilson.libraryapplication.Member;

import code.with.vanilson.libraryapplication.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BookRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
