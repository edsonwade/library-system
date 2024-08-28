package code.with.vanilson.libraryapplication.common.https;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * HeaderRequest
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderRequest {
    private Map<String, String> headers;
    private String body;

}