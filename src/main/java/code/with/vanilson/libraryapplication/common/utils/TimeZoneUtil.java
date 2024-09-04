package code.with.vanilson.libraryapplication.common.utils;

import code.with.vanilson.libraryapplication.common.constants.TimeZoneConstants;

import java.util.Random;

public class TimeZoneUtil {

    // Array to hold all the time zone constants
    private static final String[] TIME_ZONES = {
            TimeZoneConstants.ZONE_LISBON,
            TimeZoneConstants.ZONE_NEW_YORK,
            TimeZoneConstants.ZONE_LONDON,
            TimeZoneConstants.ZONE_PARIS,
            TimeZoneConstants.ZONE_TOKYO,
            TimeZoneConstants.ZONE_SYDNEY,
            TimeZoneConstants.ZONE_DUBAI,
            TimeZoneConstants.ZONE_SAO_PAULO,
            TimeZoneConstants.ZONE_MOSCOW,
            TimeZoneConstants.ZONE_HONG_KONG
            // Add more zones here if needed
    };

    // Random instance to generate random indices
    private static final Random RANDOM = new Random();

    // Method to get a random time zone
    public static String getRandomTimeZone() {
        int index = RANDOM.nextInt(TIME_ZONES.length);
        return TIME_ZONES[index];
    }

    // Private constructor to prevent instantiation
    private TimeZoneUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

}
