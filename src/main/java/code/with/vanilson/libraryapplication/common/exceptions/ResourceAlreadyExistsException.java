package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceAlreadyExistsException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@SuppressWarnings("unused") // Automatically generated serialVersionUID for Serializable class.
@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}