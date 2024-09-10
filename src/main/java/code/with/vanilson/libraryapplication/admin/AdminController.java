package code.with.vanilson.libraryapplication.admin;

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
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;

/**
 * AdminController
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-30
 */
@RestController
@RequestMapping("/api/v1/admins")
@CrossOrigin(
        origins = "http://localhost:8081", // Replace it with your frontend URL(s) if needed.
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        allowedHeaders = {"*"},
        exposedHeaders = {"*"},
        allowCredentials = "true",
        maxAge = 3600
)
@Slf4j
public class AdminController {

    public static final String X_ADMIN_ID = "X-Admin-ID";
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Retrieves a list of all admins.
     * This method responds with a list of all existing admins in the system.
     *
     * @return {@link ResponseEntity} containing a {@link List} of {@link AdminResponse} objects with the details of all admins,
     * and an HTTP status of 200 (OK). The response includes headers with security settings, content type, and optionally
     * CORS-related configurations.
     *
     * <p>
     * This method performs the following actions:
     * <ul>
     *     <li>Logs the request for retrieving all admins.</li>
     *     <li>Fetches the list of admins from the service layer.</li>
     *     <li>Prepares HTTP headers with content type, security settings, and CORS configuration.</li>
     *     <li>Returns the list of admins wrapped in a {@code ResponseEntity} with HTTP status 200 and the appropriate headers.</li>
     * </ul>
     * </p>
     */
    @GetMapping
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        log.info("Retrieves a list of all admins");

        List<AdminResponse> admins = adminService.getAllAdmins();

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(null, false);

        // Return the response entity with headers and body
        return ResponseEntity.ok()
                .headers(headers) // Apply headers
                .body(admins);
    }

    /**
     * Retrieves an admin by their unique identifier.
     * This method responds with the details of a specific admin based on the provided ID.
     *
     * @param id      The unique identifier of the admin to be retrieved.
     * @param headers Optional HTTP headers that can be included in the request (not used in this method).
     * @return {@link ResponseEntity} containing the {@link AdminResponse} with the admin details and an HTTP status of 200 (OK).
     *
     * <p>
     * This method logs the retrieval request and returns the admin details wrapped in a {@code ResponseEntity} with an HTTP status of 200.
     * If the specified admin ID does not exist, an exception should be handled by the global exception handler.
     * </p>
     */

    @GetMapping(value = "/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        AdminResponse adminResponse = adminService.getAdminById(id); // assuming this service call returns AdminResponse
        return ResponseEntity.ok()
                .headers(headers) // Use headers if necessary
                .body(adminResponse);
    }

    /**
     * Creates a new admin based on the provided admin request data.
     * This method processes a request to create an admin, logs the creation, and prepares the response with appropriate headers.
     *
     * @param adminRequest The {@link AdminRequest} containing the details for creating a new admin. Must be valid as per the validation constraints.
     * @return {@link ResponseEntity} containing the {@link AdminResponse} with the newly created admin details, HTTP status of 201 (Created),
     * and custom headers. The headers include content type, security headers, and optionally a session cookie.
     *
     * <p>
     * This method performs the following actions:
     * <ul>
     *     <li>Creates a new admin using the provided {@code adminRequest}.</li>
     *     <li>Logs the creation of the new admin, including the admin ID.</li>
     *     <li>Prepares HTTP headers with content type, security settings, and optional session cookie.</li>
     *     <li>Returns the created admin details in the response body with HTTP status 201 and the appropriate headers.</li>
     * </ul>
     * </p>
     * @throws URISyntaxException if the URI syntax is incorrect while setting the location header.
     */

    @PostMapping(value = "/create-admin")
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody @Valid AdminRequest adminRequest) throws
                                                                                                    URISyntaxException {

        AdminResponse adminResponse = adminService.createAdmin(adminRequest);

        // Log the created admin information
        log.info("Created a new admin with ID: {}", adminResponse.getId());

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(adminResponse, true);

        // Return the response entity with the AdminResponse object and headers
        return ResponseEntity
                .created(URI.create("/api/v1/admins/" + adminResponse.getId()))
                .eTag(adminResponse.getAdminCode())
                .headers(headers)
                .body(adminResponse);
    }

    /**
     * Updates an existing admin.
     *
     * @param adminId      The ID of the admin to be updated.
     * @param adminRequest The new admin data.
     * @return ResponseEntity containing the updated AdminResponse.
     */
    @PutMapping(value = "/update-admin/{adminId}")
    public ResponseEntity<AdminResponse> updateAdmin(
            @PathVariable Long adminId,
            @RequestBody @Valid AdminRequest adminRequest) {

        // Call the service to update the admin
        AdminResponse updatedAdminResponse = adminService.updateAdmin(adminRequest, adminId);

        // Log the updated admin information
        log.info("Updated admin with ID: {}", updatedAdminResponse.getId());

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(updatedAdminResponse, false);

        // Return the response entity with the AdminResponse object and headers
        return ResponseEntity
                .ok()
                .eTag(updatedAdminResponse.getAdminCode())
                .headers(headers)
                .body(updatedAdminResponse);
    }

    /**
     * Deletes an existing admin by ID and returns a success message.
     *
     * @param adminId The ID of the admin to be deleted.
     * @return ResponseEntity containing a message indicating the result of the deletion operation.
     */
    @DeleteMapping("/delete-admin/{adminId}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long adminId) {
        // Call the service to delete the admin
        adminService.deleteAdmin(adminId);

        // Return a success message indicating the deletion was successful
        var message = MessageFormat.format(MessageProvider.getMessage("library.admin.deletion_success"), adminId);
        log.info(message);
        return ResponseEntity.ok(message);
    }

    /**
     * Prepares HTTP headers for the response based on the provided admin data and cookie inclusion flag.
     * This method sets standard security headers, CORS headers, and optionally includes custom headers
     * specific to an {@link AdminResponse} object and a {@code Set-Cookie} header.
     *
     * @param adminResponse The {@link AdminResponse} object used to set custom headers specific to the admin details.
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

    private HttpHeaders prepareResponseHeaders(AdminResponse adminResponse, boolean includeCookie) {
        HttpHeaders headers = new HttpHeaders();
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

        if (adminResponse != null) {
            // Custom headers (if the response contains resource-specific data)
            headers.set(HeaderConstants.X_ADMIN_ID, String.valueOf(adminResponse.getId()));
            headers.set(HeaderConstants.X_ADMIN_NAME, adminResponse.getName());
            headers.set(HeaderConstants.X_ADMIN_EMAIL, adminResponse.getEmail());
            headers.set(HeaderConstants.X_ADMIN_CODE, adminResponse.getAdminCode());
        }

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

}