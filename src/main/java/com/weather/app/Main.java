package com.weather.app;

import com.weather.app.ui.WeatherGUI;

import javax.swing.*;

/**
 * Main entry point for the Weather Information App
 */
public class Main {
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If system look and feel fails, continue with default
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            WeatherGUI gui = new WeatherGUI();
            gui.setVisible(true);
        });
    }
}
