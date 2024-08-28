package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotPermittedException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceNotPermittedException extends RuntimeException {
    public ResourceNotPermittedException(String message) {
        super(message);
    }
}
