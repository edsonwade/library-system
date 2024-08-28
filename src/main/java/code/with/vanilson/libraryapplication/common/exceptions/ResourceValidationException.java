package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceValidationException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Validation failed")
public class ResourceValidationException extends RuntimeException {
    public ResourceValidationException(String message) {
        super(message);
    }
}