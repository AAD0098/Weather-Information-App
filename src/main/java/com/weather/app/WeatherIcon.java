package com.weather.app;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for getting weather icons
 */
public class WeatherIcon {
    
    /**
     * Get icon for weather condition
     * Uses Unicode symbols for weather icons
     */
    public static String getIconSymbol(String weatherCode) {
        if (weatherCode == null) {
            return "❓";
        }

        int code;
        try {
            code = Integer.parseInt(weatherCode);
        } catch (NumberFormatException e) {
            return "❓";
        }

        switch (code) {
            case 0:
                return "☀"; // Clear
            case 1:
            case 2:
            case 3:
                return "⛅"; // Partly cloudy
            case 45:
            case 48:
                return "🌫"; // Fog
            case 51:
            case 53:
            case 55:
            case 61:
            case 63:
            case 65:
            case 80:
            case 81:
            case 82:
                return "🌧"; // Rain
            case 66:
            case 67:
                return "🌨"; // Freezing rain
            case 71:
            case 73:
            case 75:
            case 77:
            case 85:
            case 86:
                return "❄"; // Snow
            case 95:
            case 96:
            case 99:
                return "⛈"; // Thunderstorm
            default:
                return "🌤"; // Default
        }
    }

    /**
     * Get icon label with large font
     */
    public static JLabel getIconLabel(String weatherCode) {
        JLabel iconLabel = new JLabel(getIconSymbol(weatherCode));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return iconLabel;
    }

    /**
     * Get smaller icon label for forecast
     */
    public static JLabel getSmallIconLabel(String weatherCode) {
        JLabel iconLabel = new JLabel(getIconSymbol(weatherCode));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return iconLabel;
    }
}
