package code.with.vanilson.libraryapplication.common.exceptions.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MessageProvider
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
public class MessageProvider {
    private static final String PROPERTIES_FILE = "messages.properties";// Only the file name
    private static final Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(MessageProvider.class.getName());

    /**
     * Initializes the MessageProvider class by loading the properties file into the property object.
     * If an IOException occurs during the loading process, it is logged as a severe error.
     */

    static {
        try (InputStream inputStream = MessageProvider.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                logger.log(Level.SEVERE, "Properties file not found: " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading properties file: " + PROPERTIES_FILE, e);
        }
    }

    /**
     * Retrieves a message from the properties file associated with the provided key.
     * If the key is not found in the properties file, a default message indicating the key is returned.
     *
     * @param key The key associated with the message in the properties file.
     * @return The message associated with the key, or a default message if the key is not found.
     */
    public static String getMessage(String key) {
        return properties.getProperty(key, "Message not found for key: " + key);
    }

    /**
     * Retrieves a message from the properties file associated with the provided key,
     * replacing any placeholders in the message with the provided arguments.
     *
     * @param key  The key associated with the message in the properties file.
     * @param args The arguments to replace placeholders in the message.
     * @return The formatted message with replaced placeholders, or the original message if no placeholders are found.
     */

    public static String getMessage(String key, Object... args) {
        String message = properties.getProperty(key);
        if (message != null && args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }

    private MessageProvider() {
        // Private constructor to prevent instantiation
    }
}