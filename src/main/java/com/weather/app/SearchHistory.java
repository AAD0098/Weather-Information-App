package com.weather.app;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Manages search history for cities
 */
public class SearchHistory {
    private static final int MAX_HISTORY_SIZE = 10;
    private LinkedHashSet<String> history;

    public SearchHistory() {
        this.history = new LinkedHashSet<>();
    }

    /**
     * Add a city to search history
     */
    public void addCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return;
        }

        // Remove if already exists to re-add at the end
        history.remove(city);
        history.add(city);

        // Keep only the last MAX_HISTORY_SIZE entries
        if (history.size() > MAX_HISTORY_SIZE) {
            String first = history.iterator().next();
            history.remove(first);
        }
    }

    /**
     * Get search history as a list
     */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * Clear all search history
     */
    public void clear() {
        history.clear();
    }
}
