package com.weather.app.model;

import java.util.List;

/**
 * Model class representing weather data from the Open Meteo API
 */
public class WeatherData {
    private double temperature;
    private double humidity;
    private double windSpeed;
    private int weatherCode;
    private List<ForecastEntry> forecast;
    private String cityName;
    
    public WeatherData() {
    }
    
    public WeatherData(double temperature, double humidity, double windSpeed, int weatherCode) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCode = weatherCode;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getHumidity() {
        return humidity;
    }
    
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    
    public double getWindSpeed() {
        return windSpeed;
    }
    
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    public int getWeatherCode() {
        return weatherCode;
    }
    
    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }
    
    public List<ForecastEntry> getForecast() {
        return forecast;
    }
    
    public void setForecast(List<ForecastEntry> forecast) {
        this.forecast = forecast;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    /**
     * Inner class representing a single forecast entry
     */
    public static class ForecastEntry {
        private String time;
        private double temperature;
        private int weatherCode;
        
        public ForecastEntry(String time, double temperature, int weatherCode) {
            this.time = time;
            this.temperature = temperature;
            this.weatherCode = weatherCode;
        }
        
        public String getTime() {
            return time;
        }
        
        public void setTime(String time) {
            this.time = time;
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }
        
        public int getWeatherCode() {
            return weatherCode;
        }
        
        public void setWeatherCode(int weatherCode) {
            this.weatherCode = weatherCode;
        }
        
        @Override
        public String toString() {
            return String.format("%s - %.1f°C", time, temperature);
        }
    }
}
