package code.with.vanilson.libraryapplication.admin;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

/**
 * AdminResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-30
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponse extends RepresentationModel<AdminResponse> {
    private Long id;
    private String name;
    private String email;
    private AddressDTO address;
    private String contact;
    private String adminCode;
    private Role role;
}