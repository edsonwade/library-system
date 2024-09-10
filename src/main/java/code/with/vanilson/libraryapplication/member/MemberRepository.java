package code.with.vanilson.libraryapplication.member;

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
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);

    boolean existsMemberByEmailAndIdNot(String email, Long memberId);

    boolean existsMemberByContactAndIdNot(String contact, Long memberId);

}
