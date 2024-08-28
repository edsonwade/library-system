package code.with.vanilson.libraryapplication.Person;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AddressDTO
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddressDTO {
    @NotNull(message = "Street must not be null")
    @NotBlank(message = "Street must not be blank")
    @Size(max = 255, message = "Street must be no longer than 255 characters")
    private String street;

    @NotNull(message = "City must not be null")
    @NotBlank(message = "City must not be blank")
    @Size(max = 100, message = "City must be no longer than 100 characters")
    private String city;

    @NotNull(message = "State must not be null")
    @NotBlank(message = "State must not be blank")
    @Size(max = 100, message = "State must be no longer than 100 characters")
    private String state;

    @NotNull(message = "Country must not be null")
    @NotBlank(message = "Country must not be blank")
    @Size(max = 100, message = "Country must be no longer than 100 characters")
    private String country;

    @NotNull(message = "Postal code must not be null")
    @NotBlank(message = "Postal code must not be blank")
    @Size(max = 20, message = "Postal code must be no longer than 20 characters")
    private String postalCode;
}