package code.with.vanilson.libraryapplication.security;

import code.with.vanilson.libraryapplication.user.UserRepository;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static code.with.vanilson.libraryapplication.util.constant.SecurityConstants.AUTHORIZATION_HEADER;
import static code.with.vanilson.libraryapplication.util.constant.SecurityConstants.BEARER_PREFIX;
import static io.jsonwebtoken.lang.Strings.EMPTY;

@Component
@Slf4j
@SuppressWarnings("all")
/**
 * SecurityFilter for intercepting requests and extracting JWT tokens from Authorization header.
 */
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException, ServletException {
        String token = recoverToken(request);
        if (token != null) {
            authenticateUser(token);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * <p>
     * This method checks if the Authorization header is present and formatted correctly
     * with a "Bearer " prefix. If the header is valid, it removes the "Bearer " prefix
     * and returns the token. If the header is missing or incorrectly formatted, it logs
     * an error and returns null.
     *
     * @param request the HttpServletRequest containing the Authorization header
     * @return the extracted JWT token, or null if the header is missing or invalid
     */

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.error("Token not found or invalid format");
            return null;
        }
        log.info("Token found: {}", authHeader);
        return authHeader.replace(BEARER_PREFIX, EMPTY);
    }

    /**
     * Authenticates a user based on the provided JWT token.
     * <p>
     * This method validates the given token using the JWT token service. If the token is valid,
     * it retrieves the user details from the repository using the subject (user login) extracted
     * from the token. Then, it creates an authentication token and sets it in the security context.
     * If the token is invalid, a warning is logged.
     *
     * @param token the JWT token to validate and authenticate the user
     */

    private void authenticateUser(String token) {
        String subject = jwtTokenService.validateToken(token);
        if (subject != null) {
            var userDetail = userRepository.findUserByLogin(subject);
            var authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                    userDetail.getAuthorities());
            log.info("User authenticated: {}", userDetail);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("Token validation failed");
        }
    }

}
