package com.weather.app.util;

/**
 * Utility class for converting between different units
 */
public class UnitConverter {
    
    /**
     * Convert Celsius to Fahrenheit
     * @param celsius Temperature in Celsius
     * @return Temperature in Fahrenheit
     */
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }
    
    /**
     * Convert meters per second to kilometers per hour
     * @param mps Speed in meters per second
     * @return Speed in kilometers per hour
     */
    public static double mpsToKmh(double mps) {
        return mps * 3.6;
    }
    
    /**
     * Format temperature with appropriate unit symbol
     * @param temp Temperature value
     * @param isFahrenheit Whether to use Fahrenheit
     * @return Formatted temperature string
     */
    public static String formatTemperature(double temp, boolean isFahrenheit) {
        if (isFahrenheit) {
            return String.format("%.1f°F", celsiusToFahrenheit(temp));
        } else {
            return String.format("%.1f°C", temp);
        }
    }
    
    /**
     * Format wind speed with appropriate unit symbol
     * @param speed Wind speed in m/s
     * @param useKmh Whether to use km/h
     * @return Formatted wind speed string
     */
    public static String formatWindSpeed(double speed, boolean useKmh) {
        if (useKmh) {
            return String.format("%.1f km/h", mpsToKmh(speed));
        } else {
            return String.format("%.1f m/s", speed);
        }
    }
}
