package code.with.vanilson.libraryapplication.member;

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

/**
 * MemberService
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-09-06
 */
@RestController
@RequestMapping("/api/v1/members")
@CrossOrigin(
        origins = "http://localhost:8081", // Replace it with your frontend URL(s) if needed.
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        allowedHeaders = {"*"},
        exposedHeaders = {"*"},
        allowCredentials = "true",
        maxAge = 3600
)
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> listAllMembers() {
        var responses = memberService.getAllMembers();
        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(null, false);
        log.info("Received a GET request to /api/v1/member endpoint");

        // Return the response entity with headers and body
        return ResponseEntity.ok()
                .headers(headers)
                .body(responses);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id,
                                                        @RequestHeader HttpHeaders headers) {
        MemberResponse librarianResponse = memberService.getMemberById(id); // assuming this service call
        log.info("retrieve member by id {}", id);
        return ResponseEntity.ok()
                .headers(headers) // Use headers if necessary
                .body(librarianResponse);
    }

    @PostMapping(value = "/create-member")
    public ResponseEntity<MemberResponse> createNewMember(@RequestBody @Valid MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);

        // Log the created member information
        log.info("Created a new member with ID: {}", memberResponse.getId());

        // Prepare response headers
        HttpHeaders headers = prepareResponseHeaders(memberResponse, true);

        // Return the response entity with the MemberResponse object and headers
        return ResponseEntity
                .created(URI.create("/api/v1/members/" + memberResponse.getId()))
                .eTag(String.valueOf(memberResponse.getId()))
                .headers(headers)
                .body(memberResponse);
    }

    /**
     * Updates a member with full data using PUT.
     * All fields must be provided in the request body.
     *
     * @param memberId      The ID of the member to be updated.
     * @param memberRequest The full request containing updated member details.
     * @return LibrarianResponse containing the updated member data.
     */
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberRequest memberRequest) {

        // Call the service method to update the member
        MemberResponse updatedMember = memberService.updateMember(memberRequest, memberId);

        // Return the response with the updated member data
        return ResponseEntity.ok(updatedMember);
    }

    /**
     * Deletes an existing member by ID and returns a success message.
     *
     * @param memberId The ID of the member to be deleted.
     * @return ResponseEntity containing a message indicating the result of the deletion operation.
     */
    @DeleteMapping("/delete-member/{memberId}")
    public ResponseEntity<String> deleteMemberById(@PathVariable Long memberId) {
        // Call the service to delete the member
        memberService.deleteMemberById(memberId);

        // Return a success message indicating the deletion was successful
        var message =
                MessageFormat.format(MessageProvider.getMessage("library.member.deletion_success"), memberId);
        log.info(message);
        return ResponseEntity.ok(message);
    }

    /**
     * Prepares HTTP headers for the response based on the provided admin data and cookie inclusion flag.
     * This method sets standard security headers, CORS headers, and optionally includes custom headers
     * specific to an {@link MemberResponse} object and a {@code Set-Cookie} header.
     *
     * @param response      The {@link MemberResponse} object used to set custom headers specific to the admin details.
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
     *     <li>{@code X-Frame-Options} - Prevents blackjacking by disallowing the page to be framed.</li>
     *     <li>{@code X-XSS-Protection} - Enables cross-site scripting (XSS) protection in supported browsers.</li>
     *     <li>{@code Access-Control-Allow-Origin} - Specifies which origins are permitted to access the resource.</li>
     *     <li>{@code Set-Cookie} (conditionally) - Sets a session cookie if {@code includeCookie} is {@code true}.</li>
     *     <li>Custom headers related to the admin data (if {@code adminResponse} is not {@code null})</li>
     * </ul>
     * </p>
     */

    private HttpHeaders prepareResponseHeaders(MemberResponse response, boolean includeCookie) {
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

        if (response != null) {
            // Custom headers (if the response contains resource-specific data)
            headers.set(HeaderConstants.X_ADMIN_ID, String.valueOf(response.getId()));
            headers.set(HeaderConstants.X_ADMIN_NAME, response.getName());
            headers.set(HeaderConstants.X_ADMIN_EMAIL, response.getEmail());
            headers.set(HeaderConstants.X_ADMIN_CODE, response.getContact());
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