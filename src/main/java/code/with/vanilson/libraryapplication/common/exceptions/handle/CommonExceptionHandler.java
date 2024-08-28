package code.with.vanilson.libraryapplication.common.exceptions.handle;

import code.with.vanilson.libraryapplication.common.exceptions.ErrorResponse;
import org.springframework.http.ResponseEntity;

/**
 * CommonExceptionHandler
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
public interface CommonExceptionHandler {
    ResponseEntity<ErrorResponse> handleNotFoundException(String ex);

    ResponseEntity<ErrorResponse> handleBadRequestException(String ex);

    ResponseEntity<ErrorResponse> handleConflictRequestException(String ex);
}