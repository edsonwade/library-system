package code.with.vanilson.libraryapplication.Member;

import lombok.Getter;

/**
 * MembershipStatus
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@Getter
public enum MembershipStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    CANCELLED("cancelled"),
    SUSPEND("suspended");

    private final String status;

    MembershipStatus(String status) {
        this.status = status;
    }
}