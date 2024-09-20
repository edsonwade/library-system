package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.common.https.HeaderConstants;
import code.with.vanilson.libraryapplication.common.utils.MessageProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * LibrarianController
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-02
 */
@RestController
@RequestMapping("/api/v1/librarians")
@CrossOrigin(
        origins = "http://localhost:8081", // Replace it with your frontend URL(s) if needed.
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        allowedHeaders = {"*"},
        exposedHeaders = {"*"},
        allowCredentials = "true",
        maxAge = 3600
)
@Slf4j
public class LibrarianController {

    private final LibrarianService librarianService;

    public LibrarianController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @GetMapping
    public ResponseEntity<List<LibrarianResponse>> listAllLibrarians() {
        var responses = librarianService.getAllLibrarians();
        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(null, false);
        log.info("Received a GET request to /api/v1/librarian endpoint");

        // Return the response entity with headers and body
        return ResponseEntity.ok()
                .headers(headers) // Apply headers
                .body(responses);
    }

    /**
     * Retrieves a librarian by their unique identifier.
     * This method responds with the details of a specific librarian based on the provided ID.
     *
     * @param id      The unique identifier of the librarian to be retrieved.
     * @param headers Optional HTTP headers that can be included in the request (not used in this method).
     * @return {@link ResponseEntity} containing the {@link LibrarianResponse} with the librarian details and an HTTP status of 200 (OK).
     *
     * <p>
     * This method logs the retrieval request and returns the librarian details wrapped in a {@code ResponseEntity} with an HTTP status of 200.
     * If the specified librarian ID does not exist, an exception should be handled by the global exception handler.
     * </p>
     */

    @GetMapping(value = "/{id}")
    public ResponseEntity<LibrarianResponse> getLibrarianById(@PathVariable Long id,
                                                              @RequestHeader HttpHeaders headers) {
        LibrarianResponse librarianResponse = librarianService.getLibrarianById(id); // assuming this service call
        log.info("retrieve librarian by id {}", id);
        return ResponseEntity.ok()
                .headers(headers) // Use headers if necessary
                .body(librarianResponse);
    }

    /**
     * Creates a new librarian based on the provided librarian request data.
     * This method processes a request to create a librarian, logs the creation, and prepares the response with appropriate headers.
     *
     * @param librarianRequest The {@link LibrarianResponse} containing the details for creating a new librarian. Must be valid as per the validation constraints.
     * @return {@link ResponseEntity} containing the {@link LibrarianResponse} with the newly created librarian details, HTTP status of 201 (Created),
     * and custom headers. The headers include content type, security headers, and optionally a session cookie.
     *
     * <p>
     * This method performs the following actions:
     * <ul>
     *     <li>Creates a new librarian using the provided {@code librarianRequest}.</li>
     *     <li>Logs the creation of the new librarian, including the librarian ID.</li>
     *     <li>Prepares HTTP headers with content type, security settings, and optional session cookie.</li>
     *     <li>Returns the created librarian details in the response body with HTTP status 201 and the appropriate headers.</li>
     * </ul>
     * </p>
     */

    @PostMapping(value = "/create-librarian")
    public ResponseEntity<LibrarianResponse> createNewLibrarian(@RequestBody @Valid LibrarianRequest librarianRequest) {
        LibrarianResponse librarianResponse = librarianService.createLibrarian(librarianRequest);

        // Log the created librarian information
        log.info("Created a new librarian with ID: {}", librarianResponse.getId());

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(librarianResponse, true);

        // Return the response entity with the LibrarianResponse object and headers
        return ResponseEntity
                .created(URI.create("/api/v1/librarians/" + librarianResponse.getId()))
                .eTag(librarianResponse.getEmployeeCode())
                .headers(headers)
                .body(librarianResponse);
    }

    /**
     * Updates a librarian with full data using PUT.
     * All fields must be provided in the request body.
     *
     * @param librarianId      The ID of the librarian to be updated.
     * @param librarianRequest The full request containing updated librarian details.
     * @return LibrarianResponse containing the updated librarian data.
     */
    @PutMapping("/{librarianId}")
    public ResponseEntity<LibrarianResponse> updateLibrarian(
            @PathVariable Long librarianId,
            @Valid @RequestBody LibrarianRequest librarianRequest) {

        // Call the service method to update the librarian
        LibrarianResponse updatedLibrarian = librarianService.updateLibrarian(librarianRequest, librarianId);

        // Return the response with the updated librarian data
        return ResponseEntity.ok(updatedLibrarian);
    }

    /**
     * Partially updates a librarian using PATCH.
     * Only the provided fields will be updated.
     *
     * @param librarianId The ID of the librarian to be updated.
     * @return LibrarianResponse containing the updated librarian data.
     */
    @PatchMapping("/{librarianId}")
    public ResponseEntity<LibrarianResponse> patchLibrarian(
            @PathVariable Long librarianId,
            @RequestBody Map<String, Object> updates) {

        log.info("Patching librarian with ID {}", librarianId);

        // Validate the provided librarian ID
        LibrarianService.validateLibrarianIdIsPositive(librarianId);

        // Perform partial update using the provided map of updates
        LibrarianResponse updatedLibrarian = librarianService.patchLibrarian(librarianId, updates);

        // Return the updated librarian response
        return ResponseEntity.ok(updatedLibrarian);
    }

    /**
     * Deletes an existing librarian by ID and returns a success message.
     *
     * @param librarianId The ID of the librarian to be deleted.
     * @return ResponseEntity containing a message indicating the result of the deletion operation.
     */
    @DeleteMapping("/delete-librarian/{librarianId}")
    public ResponseEntity<String> deleteLibrarianById(@PathVariable Long librarianId) {
        // Call the service to delete the librarian
        librarianService.deleteLibrarianById(librarianId);

        // Return a success message indicating the deletion was successful
        var message =
                MessageFormat.format(MessageProvider.getMessage("library.librarian.deletion_success"), librarianId);
        log.info(message);
        return ResponseEntity.ok(message);
    }

    /**
     * Prepares HTTP headers for the response based on the provided admin data and cookie inclusion flag.
     * This method sets standard security headers, CORS headers, and optionally includes custom headers
     * specific to an {@link LibrarianResponse} object and a {@code Set-Cookie} header.
     *
     * @param response      The {@link LibrarianResponse} object used to set custom headers specific to the admin details.
     *                      If {@code null}, custom admin-specific headers will not be set.
     * @param includeCookie A {@code boolean} indicating whether to include a {@code Set-Cookie} header in the response.
     *                      If {@code true}, a session cookie is added; if {@code false}, no session cookie is added.
     * @return {@link HttpHeaders} containing the configured headers for the HTTP response.
     *
     * <p>
     * This method sets the following headers:
     * <ul>
     *     <li>{@code Content-Type: application/json} - Specifies that the response body contains JSON data.</li>
     *     <li>{@code Strict-Transport-Security} - Enables HTTP Strict Transport Security (HSTS) to prevent MITM attacks.</li>
     *     <li>{@code X-Content-Type-Options} - Prevents browsers from interpreting files as a different MIME type.</li>
     *     <li>{@code X-Frame-Options} - Prevents clickjacking by disallowing the page to be framed.</li>
     *     <li>{@code X-XSS-Protection} - Enables cross-site scripting (XSS) protection in supported browsers.</li>
     *     <li>{@code Access-Control-Allow-Origin} - Specifies which origins are permitted to access the resource.</li>
     *     <li>{@code Set-Cookie} (conditionally) - Sets a session cookie if {@code includeCookie} is {@code true}.</li>
     *     <li>Custom headers related to the admin data (if {@code adminResponse} is not {@code null})</li>
     * </ul>
     * </p>
     */

    private HttpHeaders prepareResponseHeaders(LibrarianResponse response, boolean includeCookie) {
        var headers = getHttpHeaders(response);

        // Optionally include Set-Cookie header based on context
        if (includeCookie) {
            ResponseCookie sessionCookie = ResponseCookie.from("sessionId", "abc123")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Strict")
                    .build();
            headers.add(HeaderConstants.SET_COOKIE, sessionCookie.toString()); // Add the cookie to headers
        }

        return headers;
    }

    private static HttpHeaders getHttpHeaders(LibrarianResponse response) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Set Content-Type correctly
        // Security headers (always include)
        headers.set(HeaderConstants.STRICT_TRANSPORT_SECURITY, HeaderConstants.STRICT_TRANSPORT_SECURITY_VALUE); // HSTS
        headers.set(HeaderConstants.X_CONTENT_TYPE_OPTIONS,
                HeaderConstants.X_CONTENT_TYPE_OPTIONS_VALUE); // Prevent MIME type sniffing
        headers.set(HeaderConstants.X_FRAME_OPTIONS, HeaderConstants.X_FRAME_OPTIONS_VALUE); // Prevent clickjacking
        headers.set(HeaderConstants.X_XSS_PROTECTION, HeaderConstants.X_XSS_PROTECTION_VALUE); // Enable XSS protection

        // CORS header (adjust based on access needs)
        headers.set(HeaderConstants.ACCESS_CONTROL_ALLOW_ORIGIN,
                HeaderConstants.ACCESS_CONTROL_ALLOW_ORIGIN_VALUE); // Allow cross-origin access from any domain

        if (response != null) {
            // Custom headers (if the response contains resource-specific data)
            headers.set(HeaderConstants.X_ADMIN_ID, String.valueOf(response.getId()));
            headers.set(HeaderConstants.X_ADMIN_NAME, response.getName());
            headers.set(HeaderConstants.X_ADMIN_EMAIL, response.getEmail());
            headers.set(HeaderConstants.X_ADMIN_CODE, response.getEmployeeCode());
        }
        return headers;
    }
}