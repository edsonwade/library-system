package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceAccessException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceAccessException extends RuntimeException {
    public ResourceAccessException(String message) {
        super(message);
    }
}
