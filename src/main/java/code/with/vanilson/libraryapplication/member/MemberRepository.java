package code.with.vanilson.libraryapplication.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * BookRepository
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByEmail(String email);

    Optional<Set<Member>> findMemberByIdIn(Set<Long> ids);  // Updated to find members by ID set

    boolean existsMemberByEmailAndIdNot(String email, Long memberId);

    boolean existsMemberByContactAndIdNot(String contact, Long memberId);

}
