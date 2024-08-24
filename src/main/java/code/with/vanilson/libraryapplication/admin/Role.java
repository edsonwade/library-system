package code.with.vanilson.libraryapplication.admin;

import lombok.Getter;

/**
 * Role
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-24
 */
@Getter
public enum Role {
    SUPER_ADMIN("superadmin"),
    SYSTEM_ADMIN("systemadmin"),
    LIBRARIAN("librarian"),
    MEMBER("member");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
}