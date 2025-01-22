package code.with.vanilson.libraryapplication.security;

import code.with.vanilson.libraryapplication.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static code.with.vanilson.libraryapplication.util.constant.SecurityConstants.EXPIRATION_TIME_IN_HOURS;
import static code.with.vanilson.libraryapplication.util.constant.SecurityConstants.ISSUER;

@Service
@Slf4j
public class JwtTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Generates a JWT token for the given user.
     * <p>
     * The token will include user login as the subject, and will be signed with the HMAC256 algorithm.
     *
     * @param user the user for whom the token is generated.
     * @return the generated JWT token.
     * @throws RuntimeException if there is an error while generating the token.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);  // Signing algorithm using HMAC256
            return JWT.create()
                    .withIssuer(ISSUER)  // Set issuer as "auth-api"
                    .withSubject(user.getLogin())  // Set user login as the subject
                    .withExpiresAt(getExpirationTime())  // Set token expiration time
                    .sign(algorithm);  // Sign the token with the algorithm
        } catch (JWTCreationException e) {
            log.error("Error while generating token: {}", e.getMessage());
            throw new RuntimeException("Error while generating token", e);
        }
    }

    /**
     * Validates the given JWT token.
     * <p>
     * This method checks the signature, expiration, and issuer of the token.
     * If the token is valid, it returns the user login (subject); otherwise, it returns null.
     *
     * @param token the JWT token to validate.
     * @return the user login (subject) if the token is valid, otherwise null.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);  // Signing algorithm for validation
            return JWT.require(algorithm)  // Require algorithm for validation
                    .withIssuer(ISSUER)  // Validate the issuer
                    .build()  // Build verifier
                    .verify(token)  // Verify the token signature and expiration
                    .getSubject();  // Return the subject (user login)
        } catch (JWTVerificationException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return null;  // Return null if token is invalid or expired
        }
    }

    /**
     * Calculates the expiration time for the JWT token.
     * <p
     * The token will expire after 2 hours from the current time.
     *
     * @return the expiration time as an Instant.
     */
    private Instant getExpirationTime() {
        return LocalDateTime.now()
                .plusHours(EXPIRATION_TIME_IN_HOURS)  // Add 2 hours to current time
                .toInstant(ZoneOffset.UTC);  // Convert to UTC Instant
    }

}
