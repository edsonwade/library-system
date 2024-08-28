package code.with.vanilson.libraryapplication.common.exceptions;

/**
 * ResourceAlreadyExistsException
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}