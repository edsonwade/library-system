package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceUnavailableException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ResourceUnavailableException extends RuntimeException {
    public ResourceUnavailableException(String message) {
        super(message);
    }
}
