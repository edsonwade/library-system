package code.with.vanilson.libraryapplication.common.exceptions.provider;

import code.with.vanilson.libraryapplication.common.exceptions.ResourceBadRequestException;
import code.with.vanilson.libraryapplication.common.exceptions.ResourceNotFoundException;
import code.with.vanilson.libraryapplication.common.exceptions.handle.CommonExceptionHandler;
import code.with.vanilson.libraryapplication.common.configs.ErrorMessageGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * LibraryExceptionHandlerProvider
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Component
public class LibraryExceptionHandlerProvider {

    private final CommonExceptionHandler commonExceptionHandler;

    public LibraryExceptionHandlerProvider(CommonExceptionHandler commonExceptionHandler) {
        this.commonExceptionHandler = commonExceptionHandler;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleBookNotFound(ResourceNotFoundException ex) {
        String errorMessage = ErrorMessageGenerator.bookNotFoundMessage(ex.getMessage());
        return commonExceptionHandler.handleNotFoundException(errorMessage);
    }

    @ExceptionHandler(ResourceBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleBookBadRequest(ResourceBadRequestException ex) {
        String errorMessage = ErrorMessageGenerator.bookNotFoundMessage(ex.getMessage());
        return commonExceptionHandler.handleBadRequestException(errorMessage);
    }

}