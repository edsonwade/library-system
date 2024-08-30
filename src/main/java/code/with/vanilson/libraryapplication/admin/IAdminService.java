package code.with.vanilson.libraryapplication.admin;

import java.util.List;

/**
 * IAdminService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
public interface IAdminService {

    /**
     * Get all admins
     *
     * @return List of admins
     */
    List<AdminResponse> getAllAdmins();

    /**
     * Get an admin by its ID
     *
     * @param id ID of the admin to be retrieved
     */
    AdminResponse getAdminById(Long id);

    /**
     * Find an admin by email
     *
     * @param email Email of the admin to be found
     * @return Admin object if found, null otherwise
     **/
    AdminResponse getAdminByEmail(String email);

    /**
     * Create a new admin
     *
     * @param adminRequest Admin object to be created
     * @return Admin object with assigned ID
     */
    AdminResponse createAdmin(AdminRequest adminRequest);

    /**
     * Update an existing admin
     *
     * @param adminRequest Admin object to be updated with new information about the admin being updated
     * @param adminId      Update method to be called to update the admin object with new information about the admin
     *                     being
     *                     updated
     */

    AdminResponse updateAdmin(AdminRequest adminRequest, Long adminId);

    /**
     * Delete a admin
     *
     * @param adminId ID of the admin to be deleted
     */
    void deleteAdmin(Long adminId);

}
