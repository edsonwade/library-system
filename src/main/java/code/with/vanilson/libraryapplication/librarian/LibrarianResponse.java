package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.admin.AdminResponse;
import code.with.vanilson.libraryapplication.person.AddressDTO;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

/**
 * LibrarianResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class LibrarianResponse extends RepresentationModel<LibrarianResponse> {

    private Long id;

    private String name;

    private String email;

    private AddressDTO address;

    private String contact;

    private String employeeCode;

    private AdminResponse admin;
}
