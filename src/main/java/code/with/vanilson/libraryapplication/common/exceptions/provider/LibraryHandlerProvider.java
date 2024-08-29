package code.with.vanilson.libraryapplication.common.exceptions.provider;

import code.with.vanilson.libraryapplication.common.exceptions.*;
import code.with.vanilson.libraryapplication.common.exceptions.handle.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * LibraryHandlerProvider
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Component
public class LibraryHandlerProvider extends GlobalExceptionHandler {

    @Override
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException message, HttpServletRequest request) {
        return super.handleNotFoundException(message,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,HttpServletRequest request) {
        return super.handleValidationExceptions(ex,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,HttpServletRequest request) {
        return super.handleConstraintViolationException(ex,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception message,HttpServletRequest request) {
        return super.handleGeneralException(message,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleBadRequestException(ResourceBadRequestException message,HttpServletRequest request) {
        return super.handleBadRequestException(message,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleConflictRequestException(ResourceConflictException message,HttpServletRequest request) {
        return super.handleConflictRequestException(message,request);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleInternalServerException(ResourceInternalServerErrorException message,HttpServletRequest request) {
        return super.handleInternalServerException(message,request);
    }
}