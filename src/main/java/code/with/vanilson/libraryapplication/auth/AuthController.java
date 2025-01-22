package code.with.vanilson.libraryapplication.auth;

import code.with.vanilson.libraryapplication.security.JwtTokenService;
import code.with.vanilson.libraryapplication.user.User;
import code.with.vanilson.libraryapplication.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static code.with.vanilson.libraryapplication.util.constant.MessageConstants.*;

@RestController
@RequestMapping("auth")
@SuppressWarnings("all")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager, JwtTokenService jwtTokenService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password());
        var authentication = this.authenticationManager.authenticate(authenticationToken);
        var token = jwtTokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new AuthResponseDTO(token));  // HTTP 200 for successful login
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequestDTO registrationRequest) {
        if (userRepository.findUserByLogin(registrationRequest.login()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(MESSAGE, USER_ALREADY_EXISTS));
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registrationRequest.password());
        User newUser = new User(registrationRequest.login(), encryptedPassword, registrationRequest.role());
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, USER_CREATED_SUCCESSFULLY));
    }

}

