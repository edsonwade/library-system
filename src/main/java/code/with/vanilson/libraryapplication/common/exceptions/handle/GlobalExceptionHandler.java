package code.with.vanilson.libraryapplication.common.exceptions.handle;

import code.with.vanilson.libraryapplication.common.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.Objects;

import static code.with.vanilson.libraryapplication.common.constants.ErrorCodes.*;
import static code.with.vanilson.libraryapplication.common.constants.TimeZoneConstants.*;
import static java.time.LocalDateTime.now;

/**
 * GlobalExceptionHandler
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@Component
public class GlobalExceptionHandler implements CommonExceptionHandler {

    @Override
    public ResponseEntity<ErrorResponse> handleNotFoundException(String ex) {
        var errorResponse = new ErrorResponse(
                ex,
                NOT_FOUND,
                ZONE_LISBON,
                Objects.requireNonNull(HttpStatus.NOT_FOUND).value(),
                now()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleBadRequestException(String ex) {
        var errorResponse = new ErrorResponse(
                ex,
                BAD_REQUEST,
                ZONE_LISBON,
                Objects.requireNonNull(HttpStatus.BAD_REQUEST).value(),
                now()

        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ErrorResponse> handleConflictRequestException(String ex) {
        var errorResponse = new ErrorResponse(
                ex,
                CONFLICT,
                ZONE_LISBON,
                Objects.requireNonNull(HttpStatus.CONFLICT).value(),
                now()

        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // Helper method to get the path from the request
    private String getPath(HttpServletRequest request) {
        return new ServletWebRequest(request)
                .getRequest()
                .getRequestURI();
    }
}