package com.weather.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to interact with Open-Meteo API for weather data
 */
public class WeatherAPI {
    private static final String GEOCODING_API = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String WEATHER_API = "https://api.open-meteo.com/v1/forecast";
    private static final int CONNECTION_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;

    /**
     * Fetch weather data for a given city name
     */
    public WeatherData getWeatherData(String cityName) throws Exception {
        // First, get coordinates for the city
        JSONObject geoData = getCoordinates(cityName);
        if (geoData == null) {
            throw new Exception("City not found: " + cityName);
        }

        double latitude = geoData.getDouble("latitude");
        double longitude = geoData.getDouble("longitude");
        String name = geoData.getString("name");

        // Fetch weather data using coordinates
        String urlString = WEATHER_API + "?latitude=" + latitude + "&longitude=" + longitude
                + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code"
                + "&timezone=auto";

        JSONObject weatherJson = makeAPIRequest(urlString);
        JSONObject current = weatherJson.getJSONObject("current");

        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(name);
        weatherData.setLatitude(latitude);
        weatherData.setLongitude(longitude);
        weatherData.setTemperature(current.getDouble("temperature_2m"));
        weatherData.setHumidity(current.getInt("relative_humidity_2m"));
        weatherData.setWindSpeed(current.getDouble("wind_speed_10m"));

        int weatherCode = current.getInt("weather_code");
        weatherData.setWeatherCode(String.valueOf(weatherCode));
        weatherData.setCondition(getWeatherCondition(weatherCode));

        return weatherData;
    }

    /**
     * Get forecast data for a given city
     */
    public List<ForecastData> getForecastData(String cityName) throws Exception {
        JSONObject geoData = getCoordinates(cityName);
        if (geoData == null) {
            throw new Exception("City not found: " + cityName);
        }

        double latitude = geoData.getDouble("latitude");
        double longitude = geoData.getDouble("longitude");

        String urlString = WEATHER_API + "?latitude=" + latitude + "&longitude=" + longitude
                + "&daily=temperature_2m_max,temperature_2m_min,weather_code"
                + "&timezone=auto&forecast_days=5";

        JSONObject forecastJson = makeAPIRequest(urlString);
        JSONObject daily = forecastJson.getJSONObject("daily");

        JSONArray dates = daily.getJSONArray("time");
        JSONArray maxTemps = daily.getJSONArray("temperature_2m_max");
        JSONArray minTemps = daily.getJSONArray("temperature_2m_min");
        JSONArray weatherCodes = daily.getJSONArray("weather_code");

        List<ForecastData> forecasts = new ArrayList<>();
        for (int i = 0; i < Math.min(5, dates.length()); i++) {
            String date = dates.getString(i);
            double maxTemp = maxTemps.getDouble(i);
            double minTemp = minTemps.getDouble(i);
            int weatherCode = weatherCodes.getInt(i);

            ForecastData forecast = new ForecastData(
                    date,
                    maxTemp,
                    minTemp,
                    getWeatherCondition(weatherCode),
                    String.valueOf(weatherCode)
            );
            forecasts.add(forecast);
        }

        return forecasts;
    }

    /**
     * Get coordinates for a city using geocoding API
     */
    private JSONObject getCoordinates(String cityName) throws Exception {
        String encodedCity = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
        String urlString = GEOCODING_API + "?name=" + encodedCity + "&count=1&language=en&format=json";

        JSONObject response = makeAPIRequest(urlString);

        if (response.has("results") && response.getJSONArray("results").length() > 0) {
            return response.getJSONArray("results").getJSONObject(0);
        }

        return null;
    }

    /**
     * Make HTTP request to API and return JSON response
     */
    private JSONObject makeAPIRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
        conn.setReadTimeout(READ_TIMEOUT_MS);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("API request failed with response code: " + responseCode);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        conn.disconnect();

        return new JSONObject(response.toString());
    }

    /**
     * Convert weather code to human-readable condition
     */
    private String getWeatherCondition(int code) {
        switch (code) {
            case 0:
                return "Clear";
            case 1:
            case 2:
            case 3:
                return "Partly Cloudy";
            case 45:
            case 48:
                return "Foggy";
            case 51:
            case 53:
            case 55:
                return "Drizzle";
            case 61:
            case 63:
            case 65:
                return "Rain";
            case 66:
            case 67:
                return "Freezing Rain";
            case 71:
            case 73:
            case 75:
                return "Snow";
            case 77:
                return "Snow Grains";
            case 80:
            case 81:
            case 82:
                return "Rain Showers";
            case 85:
            case 86:
                return "Snow Showers";
            case 95:
                return "Thunderstorm";
            case 96:
            case 99:
                return "Thunderstorm with Hail";
            default:
                return "Unknown";
        }
    }
}
