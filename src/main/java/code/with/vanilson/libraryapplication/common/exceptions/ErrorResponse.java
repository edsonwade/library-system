package code.with.vanilson.libraryapplication.common.exceptions;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ErrorResponse
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@Data
@JsonPropertyOrder(value = {"message", "status", "zone", "errorCode", "timestamp"})
public class ErrorResponse {
    private String message;
    private String status;
    private String zone;
    private int errorCode;
    private LocalDateTime timestamp;

    private ErrorResponse() {
    }

    public ErrorResponse(String message, String status, String zone, int errorCode, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.zone = zone;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }
}