package code.with.vanilson.libraryapplication.auth;

import code.with.vanilson.libraryapplication.user.Role;

public record RegistrationRequestDTO(String login, String password, Role role) {
}
