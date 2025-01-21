package code.with.vanilson.libraryapplication.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("auth")
@SuppressWarnings("all")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO auth) {
        var isAuthenticated = authenticationService.authenticateUser(auth.login(), auth.password());
        if (isAuthenticated) {
            return ResponseEntity.status(HttpStatus.OK).build();  // HTTP 200 for successful login
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Bad credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO auth) {
        var isRegistered = authenticationService.registerUser(auth);
        if (isRegistered) {
            return ResponseEntity.status(HttpStatus.CREATED).build();  // HTTP 201 for successful registration
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User already exists"));
        }
    }
}

