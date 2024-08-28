package code.with.vanilson.libraryapplication.common.configs;

/**
 * ErrorMessageGenerator
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-28
 */
public class ErrorMessageGenerator {

    private ErrorMessageGenerator() {
        //default constructor
    }

    public static String bookNotFoundMessage(String departmentName) {
        return departmentName;
    }

    public static String bookBadRequestMessage(String departmentName) {
        return departmentName;
    }

}