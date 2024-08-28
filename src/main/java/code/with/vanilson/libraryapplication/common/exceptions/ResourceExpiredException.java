package code.with.vanilson.libraryapplication.common.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * ResourceExpiredException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-20
 */
@ResponseStatus(HttpStatus.GONE)
public class ResourceExpiredException extends RuntimeException {
    public ResourceExpiredException(String message) {
        super(message);
    }
}