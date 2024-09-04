package code.with.vanilson.libraryapplication.librarian;

import java.util.List;
import java.util.Map;

/**
 * ILibrarian
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */

public interface ILibrarian {

    /**
     * Get all Librarians
     *
     * @return List of Librarians
     */
    List<LibrarianResponse> getAllLibrarians();

    /**
     * Get a Librarian by its ID
     *
     * @param id ID of the Librarian to be retrieved
     */
    LibrarianResponse getLibrarianById(Long id);

    /**
     * Find a Librarian by email
     *
     * @param email Email of the Librarian to be found
     * @return Librarian object if found, null otherwise
     **/
    LibrarianResponse getLibrarianByEmail(String email);

    /**
     * Create a new Librarian
     *
     * @param librarianRequest Librarian object to be created
     * @return Librarian object with assigned ID
     */
    LibrarianResponse createLibrarian(LibrarianRequest librarianRequest);

    /**
     * Update an existing Librarian
     *
     * @param librarianRequest Librarian object to be updated with new information about the Librarian being updated
     * @param librarianId      Update method to be called to update the Librarian object with new information about the Librarian
     *                         being
     *                         updated
     */

    LibrarianResponse updateLibrarian(LibrarianRequest librarianRequest, Long librarianId);
    // Patch method to support partial updates of a librarian object.

    LibrarianResponse patchLibrarian(Long librarianId, Map<String, Object> updates);

    /**
     * Delete a Librarian
     *
     * @param librarianId ID of the Librarian to be deleted
     */
    void deleteLibrarianById(Long librarianId);
}
