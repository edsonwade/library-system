package code.with.vanilson.libraryapplication.person;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * Address
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-22
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

}