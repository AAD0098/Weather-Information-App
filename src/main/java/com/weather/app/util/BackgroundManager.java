package com.weather.app.util;

import java.awt.Color;
import java.time.LocalTime;

/**
 * Manages dynamic background colors based on time of day
 */
public class BackgroundManager {
    
    // Time periods
    private static final LocalTime MORNING_START = LocalTime.of(6, 0);
    private static final LocalTime AFTERNOON_START = LocalTime.of(12, 0);
    private static final LocalTime EVENING_START = LocalTime.of(18, 0);
    private static final LocalTime NIGHT_START = LocalTime.of(21, 0);
    
    // Background colors
    private static final Color MORNING_COLOR = new Color(255, 223, 186);    // Light orange
    private static final Color AFTERNOON_COLOR = new Color(135, 206, 250);  // Light sky blue
    private static final Color EVENING_COLOR = new Color(255, 182, 193);    // Light pink
    private static final Color NIGHT_COLOR = new Color(25, 25, 112);        // Midnight blue
    
    /**
     * Get the background color based on current time
     * @return Color for the current time of day
     */
    public static Color getCurrentBackgroundColor() {
        LocalTime now = LocalTime.now();
        return getBackgroundColorForTime(now);
    }
    
    /**
     * Get the background color for a specific time
     * @param time The time to check
     * @return Color for the given time of day
     */
    public static Color getBackgroundColorForTime(LocalTime time) {
        if (time.isAfter(NIGHT_START) || time.isBefore(MORNING_START)) {
            return NIGHT_COLOR;
        } else if (time.isAfter(EVENING_START)) {
            return EVENING_COLOR;
        } else if (time.isAfter(AFTERNOON_START)) {
            return AFTERNOON_COLOR;
        } else {
            return MORNING_COLOR;
        }
    }
    
    /**
     * Get the current time period name
     * @return String describing the current time period
     */
    public static String getCurrentTimePeriod() {
        LocalTime now = LocalTime.now();
        return getTimePeriodForTime(now);
    }
    
    /**
     * Get the time period name for a specific time
     * @param time The time to check
     * @return String describing the time period
     */
    public static String getTimePeriodForTime(LocalTime time) {
        if (time.isAfter(NIGHT_START) || time.isBefore(MORNING_START)) {
            return "Night";
        } else if (time.isAfter(EVENING_START)) {
            return "Evening";
        } else if (time.isAfter(AFTERNOON_START)) {
            return "Afternoon";
        } else {
            return "Morning";
        }
    }
}
