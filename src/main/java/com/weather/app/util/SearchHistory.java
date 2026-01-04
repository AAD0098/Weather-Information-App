package com.weather.app.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage search history with timestamps
 */
public class SearchHistory {
    private static final int MAX_HISTORY_SIZE = 10;
    private final List<SearchEntry> history;
    private final DateTimeFormatter formatter;
    
    public SearchHistory() {
        this.history = new ArrayList<>();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * Add a search entry to history
     * @param cityName The city name that was searched
     */
    public void addSearch(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return;
        }
        
        SearchEntry entry = new SearchEntry(cityName, LocalDateTime.now());
        
        // Add to beginning of list
        history.add(0, entry);
        
        // Keep only last 10 entries
        while (history.size() > MAX_HISTORY_SIZE) {
            history.remove(history.size() - 1);
        }
    }
    
    /**
     * Get all search history entries
     * @return List of search entries
     */
    public List<SearchEntry> getHistory() {
        return new ArrayList<>(history);
    }
    
    /**
     * Get formatted history as string array for display
     * @return Array of formatted history strings
     */
    public String[] getFormattedHistory() {
        return history.stream()
                .map(entry -> String.format("%s - %s", 
                        entry.getCityName(), 
                        entry.getTimestamp().format(formatter)))
                .toArray(String[]::new);
    }
    
    /**
     * Clear all history
     */
    public void clearHistory() {
        history.clear();
    }
    
    /**
     * Inner class representing a single search entry
     */
    public static class SearchEntry {
        private final String cityName;
        private final LocalDateTime timestamp;
        
        public SearchEntry(String cityName, LocalDateTime timestamp) {
            this.cityName = cityName;
            this.timestamp = timestamp;
        }
        
        public String getCityName() {
            return cityName;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
