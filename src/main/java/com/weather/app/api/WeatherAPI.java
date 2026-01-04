package com.weather.app.api;

import com.weather.app.model.WeatherData;
import com.weather.app.model.WeatherData.ForecastEntry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for interacting with the Open Meteo API
 */
public class WeatherAPI {
    
    private static final String GEOCODING_API_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast";
    
    /**
     * Fetch weather data for a given city
     * @param cityName The name of the city
     * @return WeatherData object containing weather information
     * @throws Exception if the API call fails or the city is not found
     */
    public static WeatherData fetchWeatherData(String cityName) throws Exception {
        // Step 1: Get coordinates for the city
        double[] coordinates = getCoordinates(cityName);
        double latitude = coordinates[0];
        double longitude = coordinates[1];
        
        // Step 2: Fetch weather data using coordinates
        String weatherJson = fetchWeatherJson(latitude, longitude);
        
        // Step 3: Parse JSON and create WeatherData object
        WeatherData weatherData = parseWeatherData(weatherJson);
        weatherData.setCityName(cityName);
        
        return weatherData;
    }
    
    /**
     * Get coordinates for a city using the geocoding API
     * @param cityName The name of the city
     * @return Array containing [latitude, longitude]
     * @throws Exception if the city is not found
     */
    private static double[] getCoordinates(String cityName) throws Exception {
        String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
        String urlString = GEOCODING_API_URL + "?name=" + encodedCity + "&count=1&language=en&format=json";
        
        String jsonResponse = makeHttpRequest(urlString);
        
        // Simple JSON parsing for coordinates
        if (jsonResponse == null || jsonResponse.isEmpty() || !jsonResponse.contains("\"results\"")) {
            throw new Exception("City not found: " + cityName);
        }
        
        // Check if results array is empty
        if (jsonResponse.contains("\"results\":[]")) {
            throw new Exception("City not found: " + cityName);
        }
        
        double latitude = extractJsonValue(jsonResponse, "\"latitude\":");
        double longitude = extractJsonValue(jsonResponse, "\"longitude\":");
        
        // Validate coordinates fall within realistic geographic bounds
        if (Double.isNaN(latitude) || Double.isNaN(longitude)
                || Double.isInfinite(latitude) || Double.isInfinite(longitude)
                || latitude < -90.0 || latitude > 90.0
                || longitude < -180.0 || longitude > 180.0) {
            throw new Exception("Invalid coordinates for city: " + cityName);
        }
        
        return new double[]{latitude, longitude};
    }
    
    /**
     * Fetch weather JSON from Open Meteo API
     * @param latitude Latitude coordinate
     * @param longitude Longitude coordinate
     * @return JSON response as string
     * @throws Exception if the API call fails
     */
    private static String fetchWeatherJson(double latitude, double longitude) throws Exception {
        String urlString = String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code&hourly=temperature_2m,weather_code&timezone=auto",
            WEATHER_API_URL, latitude, longitude
        );
        
        return makeHttpRequest(urlString);
    }
    
    /**
     * Make HTTP GET request
     * @param urlString The URL to request
     * @return Response body as string
     * @throws Exception if the request fails
     */
    private static String makeHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        try {
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("HTTP request failed with code: " + responseCode);
            }
            
            StringBuilder response = new StringBuilder();
            String line;
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * Parse weather data from JSON response
     * @param json JSON response from Open Meteo API
     * @return WeatherData object
     * @throws Exception if parsing fails
     */
    private static WeatherData parseWeatherData(String json) throws Exception {
        WeatherData data = new WeatherData();
        
        // Parse current weather
        int currentIndex = json.indexOf("\"current\":");
        if (currentIndex == -1) {
            throw new Exception("Invalid JSON response");
        }
        
        String currentSection = json.substring(currentIndex);
        
        data.setTemperature(extractJsonValue(currentSection, "\"temperature_2m\":"));
        data.setHumidity(extractJsonValue(currentSection, "\"relative_humidity_2m\":"));
        data.setWindSpeed(extractJsonValue(currentSection, "\"wind_speed_10m\":"));
        data.setWeatherCode((int) extractJsonValue(currentSection, "\"weather_code\":"));
        
        // Parse hourly forecast (next 5 hours)
        List<ForecastEntry> forecast = parseForecast(json);
        data.setForecast(forecast);
        
        return data;
    }
    
    /**
     * Parse forecast data from JSON
     * @param json JSON response
     * @return List of forecast entries
     */
    private static List<ForecastEntry> parseForecast(String json) {
        List<ForecastEntry> forecast = new ArrayList<>();
        
        try {
            int hourlyIndex = json.indexOf("\"hourly\":");
            if (hourlyIndex == -1) {
                return forecast;
            }
            
            String hourlySection = json.substring(hourlyIndex);
            
            // Extract time array
            int timeIndex = hourlySection.indexOf("\"time\":[");
            if (timeIndex == -1) {
                return forecast;
            }
            String timeArray = hourlySection.substring(timeIndex + 8);
            int timeEnd = timeArray.indexOf("]");
            timeArray = timeArray.substring(0, timeEnd);
            
            // Extract temperature array
            int tempIndex = hourlySection.indexOf("\"temperature_2m\":[");
            if (tempIndex == -1) {
                return forecast;
            }
            String tempArray = hourlySection.substring(tempIndex + 18);
            int tempEnd = tempArray.indexOf("]");
            tempArray = tempArray.substring(0, tempEnd);
            
            // Extract weather code array
            int codeIndex = hourlySection.indexOf("\"weather_code\":[");
            if (codeIndex == -1) {
                return forecast;
            }
            String codeArray = hourlySection.substring(codeIndex + 16);
            int codeEnd = codeArray.indexOf("]");
            codeArray = codeArray.substring(0, codeEnd);
            
            // Parse arrays
            String[] times = timeArray.split(",");
            String[] temps = tempArray.split(",");
            String[] codes = codeArray.split(",");
            
            // Get next 5 hours
            int count = Math.min(5, times.length);
            for (int i = 0; i < count; i++) {
                String time = times[i].replaceAll("\"", "").trim();
                // Extract just the hour part
                if (time.length() >= 13) {
                    time = time.substring(11, 13) + ":00";
                }
                double temp = Double.parseDouble(temps[i].trim());
                int code = (int) Double.parseDouble(codes[i].trim());
                
                forecast.add(new ForecastEntry(time, temp, code));
            }
        } catch (Exception e) {
            // Return empty forecast on error
        }
        
        return forecast;
    }
    
    /**
     * Extract a numeric value from JSON string
     * @param json JSON string
     * @param key The key to search for
     * @return The numeric value, or 0.0 if not found or invalid
     */
    private static double extractJsonValue(String json, String key) {
        if (json == null || json.isEmpty() || key == null) {
            return 0.0;
        }
        
        int index = json.indexOf(key);
        if (index == -1) {
            return 0.0;
        }
        
        String remaining = json.substring(index + key.length());
        int endIndex = remaining.indexOf(",");
        int endIndex2 = remaining.indexOf("}");
        
        if (endIndex == -1) {
            endIndex = endIndex2;
        } else if (endIndex2 != -1 && endIndex2 < endIndex) {
            endIndex = endIndex2;
        }
        
        if (endIndex == -1) {
            return 0.0;
        }
        
        String valueStr = remaining.substring(0, endIndex).trim();
        
        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    /**
     * Get weather description from weather code
     * @param code Weather code from Open Meteo
     * @return Human-readable weather description
     */
    public static String getWeatherDescription(int code) {
        if (code == 0) return "Clear sky";
        if (code <= 3) return "Partly cloudy";
        if (code <= 48) return "Foggy";
        if (code <= 67) return "Rainy";
        if (code <= 77) return "Snowy";
        if (code <= 82) return "Rain showers";
        if (code <= 86) return "Snow showers";
        if (code <= 99) return "Thunderstorm";
        return "Unknown";
    }
    
    /**
     * Get icon filename for weather code
     * @param code Weather code from Open Meteo
     * @return Icon filename
     */
    public static String getWeatherIcon(int code) {
        if (code == 0) return "clear.png";
        if (code <= 3) return "cloudy.png";
        if (code <= 48) return "cloudy.png";
        if (code <= 67) return "rain.png";
        if (code <= 77) return "rain.png";
        if (code <= 82) return "rain.png";
        if (code <= 86) return "rain.png";
        if (code <= 99) return "storm.png";
        return "clear.png";
    }
}
