package code.with.vanilson.libraryapplication.librarian;

import code.with.vanilson.libraryapplication.person.AddressDTO;
import code.with.vanilson.libraryapplication.admin.AdminResponse;
import lombok.Builder;
import lombok.Data;

/**
 * LibrarianResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Data
@Builder
public class LibrarianResponse {

    private Long id;

    private String name;

    private String email;

    private AddressDTO address;

    private String contact;

    private String employeeCode;

    private AdminResponse admin;
}
