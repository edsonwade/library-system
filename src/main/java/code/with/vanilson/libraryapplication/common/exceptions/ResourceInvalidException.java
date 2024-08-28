package code.with.vanilson.libraryapplication.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * ResourceInvalidException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceInvalidException extends RuntimeException {
    public ResourceInvalidException(String message) {
        super(message);
    }
}
