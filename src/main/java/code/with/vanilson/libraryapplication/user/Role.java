package code.with.vanilson.libraryapplication.user;

import lombok.Getter;

/**
 * Role
 *
 * @author vamuhong
 * @version 1.0
 * @since 2025-01-20
 */
@Getter
public enum Role {
    ADMIN("admin"),
    USER("user");
    private final String userRole;

    Role(String role) {
        this.userRole = role;
    }
}