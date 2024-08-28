package code.with.vanilson.libraryapplication.common.utils;

import java.util.List;

/**
 * NumbersUtil
 *
 * @author vamuhong
 * @version 1.0
 * @since 2024-08-26
 */
public class NumbersUtil {

    // Integer Constants
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int TEN = 10;

    public static final int ELEVEN = 11;
    public static final int TWELVE = 12;
    public static final int THIRTEEN = 13;
    public static final int FOURTEEN = 14;
    public static final int FIFTEEN = 15;
    public static final int SIXTEEN = 16;
    public static final int SEVENTEEN = 17;
    public static final int EIGHTEEN = 18;
    public static final int NINETEEN = 19;
    public static final int TWENTY = 20;

    // Double Constants
    public static final double PI = 3.14159265358979323846;
    public static final double EULER_CONSTANT = 2.71828182845904523536;
    public static final double GOLDEN_RATIO = 1.61803398874989484820;

    public static final double SQUARE_ROOT_OF_TWO = 1.41421356237309504880;
    public static final double SQUARE_ROOT_OF_THREE = 1.73205080756887729352;
    public static final double SQUARE_ROOT_OF_FIVE = 2.23606797749978969641;
    public static final double SQUARE_ROOT_OF_SEVEN = 2.64575131106459059050;
    public static final double SQUARE_ROOT_OF_ELEVEN = 3.31662479035539984911;

    // Long Constants
    public static final long MAX_LONG = Long.MAX_VALUE;
    public static final long MIN_LONG = Long.MIN_VALUE;

    public static final long BILLION = 1_000_000_000L;
    public static final long TRILLION = 1_000_000_000_000L;
    public static final long QUADRILLION = 1_000_000_000_000_000L;

    // Constant for milliseconds in a day
    public static final long MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;

    // Constants for number formats
    public static final int DEFAULT_DECIMAL_PLACES = 2;
    public static final double PERCENTAGE_CONVERSION_FACTOR = 100.0;

    // Constants for financial calculations
    public static final double DEFAULT_FINE_RATE_PER_DAY = 2.0; // Euro per day

    // Float Constants
    public static final float MAX_FLOAT = Float.MAX_VALUE;
    public static final float MIN_FLOAT = Float.MIN_VALUE;

    public static final float SQUARE_ROOT_OF_TWO_FLOAT = 1.41421356f;
    public static final float SQUARE_ROOT_OF_THREE_FLOAT = 1.73205081f;
    public static final float SQUARE_ROOT_OF_FIVE_FLOAT = 2.23606798f;
    public static final float SQUARE_ROOT_OF_SEVEN_FLOAT = 2.64575131f;
    public static final float SQUARE_ROOT_OF_ELEVEN_FLOAT = 3.31662479f;

    /**
     * Calculates the number of days between two dates.
     *
     * @param startDate The start date.
     * @param endDate   The end date.
     * @return The number of days between the two dates.
     */
    public static long calculateDaysBetween(java.util.Date startDate, java.util.Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        long differenceInMilliseconds = endDate.getTime() - startDate.getTime();
        return differenceInMilliseconds / MILLISECONDS_IN_A_DAY;
    }

    /**
     * Checks if a number is positive.
     *
     * @param number The number to check.
     * @return {@code true} if the number is positive, {@code false} otherwise.
     */
    public static boolean isPositive(int number) {
        return number > 0;
    }

    /**
     * Checks if a number is negative.
     *
     * @param number The number to check.
     * @return {@code true} if the number is negative, {@code false} otherwise.
     */
    public static boolean isNegative(int number) {
        return number < 0;
    }

    /**
     * Converts a number from one unit to another.
     *
     * @param value            The value to convert.
     * @param conversionFactor The factor to multiply the value by.
     * @return The converted value.
     */
    public static double convert(double value, double conversionFactor) {
        return value * conversionFactor;
    }

    /**
     * Rounds a number to a specified number of decimal places.
     *
     * @param value         The value to round.
     * @param decimalPlaces The number of decimal places to round to.
     * @return The rounded value.
     */
    public static double roundToDecimalPlaces(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places cannot be negative");
        }
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    /**
     * Checks if a given value is within a specified range.
     *
     * @param value The value to check.
     * @param min   The minimum value of the range (inclusive).
     * @param max   The maximum value of the range (inclusive).
     * @return {@code true} if the value is within the range, {@code false} otherwise.
     */
    public static boolean isWithinRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Converts a percentage to a decimal.
     *
     * @param percentage The percentage value (e.g., 25 for 25%).
     * @return The decimal representation of the percentage.
     */
    public static double percentageToDecimal(double percentage) {
        return percentage / 100;
    }

    /**
     * Converts a decimal to a percentage.
     *
     * @param decimal The decimal value (e.g., 0.25 for 25%).
     * @return The percentage representation of the decimal.
     */
    public static double decimalToPercentage(double decimal) {
        return decimal * 100;
    }

    /**
     * Calculates the percentage of a total amount.
     *
     * @param part  The part value.
     * @param total The total amount.
     * @return The percentage of the total amount that the part represents.
     */
    public static double calculatePercentage(double part, double total) {
        if (total == 0) {
            throw new ArithmeticException("Total cannot be zero");
        }
        return (part / total) * 100;
    }

    /**
     * Calculates the average of a list of numbers.
     *
     * @param numbers The list of numbers.
     * @return The average of the numbers.
     */
    public static double calculateAverage(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        return calculateSum(numbers) / numbers.size();
    }

    /**
     * Finds the maximum value in a list of numbers.
     *
     * @param numbers The list of numbers.
     * @return The maximum value.
     */
    public static double findMax(List<Double> numbers) {
        return findExtrema(numbers, true);
    }

    /**
     * Finds the minimum value in a list of numbers.
     *
     * @param numbers The list of numbers.
     * @return The minimum value.
     */
    public static double findMin(List<Double> numbers) {
        return findExtrema(numbers, false);
    }

    /**
     * Finds the maximum or minimum value in a list of numbers based on the `findMax` flag.
     *
     * @param numbers The list of numbers.
     * @param findMax Flag indicating whether to find the maximum or minimum value.
     * @return The maximum or minimum value.
     */
    private static double findExtrema(List<Double> numbers, boolean findMax) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        double extrema = numbers.get(0);
        for (double number : numbers) {
            if (findMax ? number > extrema : number < extrema) {
                extrema = number;
            }
        }
        return extrema;
    }

    /**
     * Calculates the sum of a list of numbers.
     *
     * @param numbers The list of numbers.
     * @return The sum of the numbers.
     */
    public static double calculateSum(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("List of numbers cannot be null or empty");
        }
        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum;
    }

    /**
     * Checks if a number is a valid integer (within the Java integer range).
     *
     * @param number The number to check.
     * @return {@code true} if the number is a valid integer, {@code false} otherwise.
     */
    public static boolean isValidInteger(double number) {
        return number >= Integer.MIN_VALUE && number <= Integer.MAX_VALUE;
    }

}