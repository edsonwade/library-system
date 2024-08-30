package code.with.vanilson.libraryapplication.common.exceptions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ErrorResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@Data
@JsonPropertyOrder(value = {"message", "status", "errorCode", "path", "zone", "timestamp", "code", "traceId"})
@SuppressWarnings("warnings")
public class ErrorResponse {
    private String message;
    private String status;
    private String zone;
    private int errorCode;
    private String path;
    private LocalDateTime timestamp;
    private String code;
    private String traceId;

    private ErrorResponse() {
        // Private constructor to prevent instantiation from outside the class.
        // Use the builder pattern or create a factory method to instantiate ErrorResponse objects.
    }

    public ErrorResponse(String message, String status, String zone, int errorCode, String path,
                         LocalDateTime timestamp,
                         String code, String traceId) {
        this.message = message;
        this.status = status;
        this.zone = zone;
        this.errorCode = errorCode;
        this.path = path;
        this.timestamp = timestamp;
        this.code = code;
        this.traceId = traceId;
    }
}