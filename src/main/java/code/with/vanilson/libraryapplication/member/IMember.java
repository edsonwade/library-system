package code.with.vanilson.libraryapplication.member;

import java.util.List;

/**
 * IMember
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-06
 */
public interface IMember {
    /**
     * Get allMembers
     *
     * @return List ofMembers
     */
    List<MemberResponse> getAllMembers();

    /**
     * Get aMember by its ID
     *
     * @param id ID of theMember to be retrieved
     */
    MemberResponse getMemberById(Long id);

    /**
     * Find aMember by email
     *
     * @param email Email of theMember to be found
     * @return member object if found, null otherwise
     **/
    MemberResponse getMemberByEmail(String email);

    /**
     * Create a newMember
     *
     * @param memberRequest object to be created
     * @return member object with assigned ID
     */
    MemberResponse createMember(MemberRequest memberRequest);

    /**
     * Update an existingMember
     *
     * @param memberRequest object to be updated with new information about theMember being updated
     * @param memberId      Update method to be called to update theMember object with new information about theMember
     *                      being
     *                      updated
     */

    MemberResponse updateMember(MemberRequest memberRequest, Long memberId);
    // Patch method to support partial updates of a member object.

    /**
     * Delete aMember
     *
     * @param memberId ID of theMember to be deleted
     */
    void deleteMemberById(Long memberId);
}