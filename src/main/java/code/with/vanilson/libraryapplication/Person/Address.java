package code.with.vanilson.libraryapplication.Person;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Address {
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;

}