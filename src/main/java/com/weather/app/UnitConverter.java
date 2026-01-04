package com.weather.app;

/**
 * Utility class for unit conversions
 */
public class UnitConverter {
    
    /**
     * Convert Celsius to Fahrenheit
     */
    public static double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    /**
     * Convert Fahrenheit to Celsius
     */
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    /**
     * Convert km/h to mph
     */
    public static double kmhToMph(double kmh) {
        return kmh * 0.621371;
    }

    /**
     * Convert mph to km/h
     */
    public static double mphToKmh(double mph) {
        return mph / 0.621371;
    }

    /**
     * Format temperature with unit
     */
    public static String formatTemperature(double temp, boolean isCelsius) {
        if (isCelsius) {
            return String.format("%.1f°C", temp);
        } else {
            return String.format("%.1f°F", celsiusToFahrenheit(temp));
        }
    }

    /**
     * Format wind speed with unit
     */
    public static String formatWindSpeed(double speed, boolean isMetric) {
        if (isMetric) {
            return String.format("%.1f km/h", speed);
        } else {
            return String.format("%.1f mph", kmhToMph(speed));
        }
    }
}
