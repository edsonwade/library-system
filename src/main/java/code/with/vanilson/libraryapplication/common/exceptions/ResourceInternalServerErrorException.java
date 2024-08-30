package code.with.vanilson.libraryapplication.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceInternalServerErrorException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-29
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceInternalServerErrorException extends RuntimeException{
    public ResourceInternalServerErrorException(String message) {
        super(message);
    }
}