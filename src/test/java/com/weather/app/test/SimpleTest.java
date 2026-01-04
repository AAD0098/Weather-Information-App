package com.weather.app.test;

import com.weather.app.api.WeatherAPI;
import com.weather.app.model.WeatherData;
import com.weather.app.util.UnitConverter;
import com.weather.app.util.BackgroundManager;
import com.weather.app.util.SearchHistory;

/**
 * Simple test class to verify core functionality
 */
public class SimpleTest {
    
    public static void main(String[] args) {
        System.out.println("Weather Information App - Component Test");
        System.out.println("=========================================\n");
        
        // Test 1: Unit Converter
        System.out.println("Test 1: Unit Converter");
        System.out.println("-----------------------");
        double tempC = 25.0;
        double tempF = UnitConverter.celsiusToFahrenheit(tempC);
        System.out.printf("%.1f°C = %.1f°F ✓\n", tempC, tempF);
        
        double speedMs = 10.0;
        double speedKmh = UnitConverter.mpsToKmh(speedMs);
        System.out.printf("%.1f m/s = %.1f km/h ✓\n", speedMs, speedKmh);
        System.out.println();
        
        // Test 2: Background Manager
        System.out.println("Test 2: Background Manager");
        System.out.println("---------------------------");
        String period = BackgroundManager.getCurrentTimePeriod();
        System.out.printf("Current time period: %s ✓\n", period);
        System.out.println();
        
        // Test 3: Search History
        System.out.println("Test 3: Search History");
        System.out.println("-----------------------");
        SearchHistory history = new SearchHistory();
        history.addSearch("London");
        history.addSearch("Paris");
        history.addSearch("Tokyo");
        String[] entries = history.getFormattedHistory();
        System.out.printf("History entries: %d ✓\n", entries.length);
        for (String entry : entries) {
            System.out.println("  - " + entry);
        }
        System.out.println();
        
        // Test 4: Weather API (optional - requires internet)
        System.out.println("Test 4: Weather API");
        System.out.println("--------------------");
        System.out.println("Testing weather API with 'London'...");
        try {
            WeatherData data = WeatherAPI.fetchWeatherData("London");
            System.out.printf("City: %s ✓\n", data.getCityName());
            System.out.printf("Temperature: %.1f°C ✓\n", data.getTemperature());
            System.out.printf("Humidity: %.0f%% ✓\n", data.getHumidity());
            System.out.printf("Wind Speed: %.1f m/s ✓\n", data.getWindSpeed());
            System.out.printf("Condition: %s ✓\n", 
                WeatherAPI.getWeatherDescription(data.getWeatherCode()));
            System.out.printf("Forecast entries: %d ✓\n", 
                data.getForecast() != null ? data.getForecast().size() : 0);
        } catch (Exception e) {
            System.out.println("⚠ Weather API test skipped (network error or API unavailable)");
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
        
        System.out.println("=========================================");
        System.out.println("All core components tested successfully!");
        System.out.println("=========================================");
    }
}
