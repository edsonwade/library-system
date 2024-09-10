package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import lombok.Builder;
import lombok.Data;

/**
 * AdminResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-30
 */
@Builder
@Data
public class AdminResponse {
    private Long id;
    private String name;
    private String email;
    private AddressDTO address;
    private String contact;
    private String adminCode;
    private Role role;
}