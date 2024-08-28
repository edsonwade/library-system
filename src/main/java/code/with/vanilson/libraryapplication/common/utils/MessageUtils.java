package code.with.vanilson.libraryapplication.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * MessageUtils
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-07-05
 */
@Component
public class MessageUtils {
    private final MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}