package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceDatabaseException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Database error")
public class ResourceDatabaseException extends RuntimeException {
    public ResourceDatabaseException(String message) {
        super(message);
    }
}