package com.weather.app;

import java.awt.Color;
import java.util.Calendar;

/**
 * Utility class for dynamic backgrounds based on time of day
 */
public class TimeBasedBackground {
    
    /**
     * Get background color based on current time of day
     */
    public static Color getBackgroundColor() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        
        return getBackgroundColorForHour(hour);
    }

    /**
     * Get background color for a specific hour
     */
    public static Color getBackgroundColorForHour(int hour) {
        // Early morning (5-7): Dawn colors
        if (hour >= 5 && hour < 7) {
            return new Color(255, 183, 77); // Orange/Yellow dawn
        }
        // Morning (7-12): Light blue sky
        else if (hour >= 7 && hour < 12) {
            return new Color(135, 206, 250); // Light sky blue
        }
        // Afternoon (12-17): Bright blue
        else if (hour >= 12 && hour < 17) {
            return new Color(70, 130, 180); // Steel blue
        }
        // Evening (17-19): Dusk colors
        else if (hour >= 17 && hour < 19) {
            return new Color(255, 140, 0); // Dark orange
        }
        // Night (19-5): Dark blue/purple
        else {
            return new Color(25, 25, 112); // Midnight blue
        }
    }

    /**
     * Get text color that contrasts with background
     */
    public static Color getTextColor() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        
        // Use white text for darker backgrounds (night time)
        if (hour >= 19 || hour < 5) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }
}
