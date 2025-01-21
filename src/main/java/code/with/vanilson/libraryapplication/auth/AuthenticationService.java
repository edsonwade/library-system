package code.with.vanilson.libraryapplication.auth;

import code.with.vanilson.libraryapplication.user.User;
import code.with.vanilson.libraryapplication.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateUser(String login, String password) {
        try {
            var usernamePasswordToken = new UsernamePasswordAuthenticationToken(login, password);
            authenticationManager.authenticate(usernamePasswordToken);  // Authenticate the user
            return true;  // Authentication success
        } catch (BadCredentialsException e) {
            return false;  // Authentication failure due to bad credentials
        }
    }

    public boolean registerUser(RegisterDTO registerDTO) {
        // Check if user already exists
        if (userRepository.findUserByLogin(registerDTO.login()) != null) {
            return false;  // User already exists
        }
        // Encrypt password and save the user
        String encodedPassword = passwordEncoder.encode(registerDTO.password());
        var newUser = new User(registerDTO.login(), encodedPassword, registerDTO.role());
        userRepository.save(newUser);
        return true;  // Registration success
    }
}
