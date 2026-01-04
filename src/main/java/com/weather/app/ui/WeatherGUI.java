package com.weather.app.ui;

import com.weather.app.api.WeatherAPI;
import com.weather.app.model.WeatherData;
import com.weather.app.util.BackgroundManager;
import com.weather.app.util.SearchHistory;
import com.weather.app.util.UnitConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Main GUI class for the Weather Information App
 */
public class WeatherGUI extends JFrame {
    
    private JTextField cityTextField;
    private JButton fetchButton;
    private JLabel temperatureLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
    private JLabel conditionLabel;
    private JLabel iconLabel;
    private JTextArea forecastArea;
    private JComboBox<String> unitSelector;
    private JList<String> historyList;
    private DefaultListModel<String> historyListModel;
    private JPanel mainPanel;
    
    private SearchHistory searchHistory;
    private WeatherData currentWeatherData;
    private boolean useFahrenheit = false;
    
    public WeatherGUI() {
        searchHistory = new SearchHistory();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateBackground();
    }
    
    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        setTitle("Weather Information App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Input components
        cityTextField = new JTextField(20);
        fetchButton = new JButton("Fetch Weather");
        
        // Display labels
        temperatureLabel = new JLabel("Temperature: --");
        temperatureLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        humidityLabel = new JLabel("Humidity: --");
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        windLabel = new JLabel("Wind Speed: --");
        windLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        conditionLabel = new JLabel("Condition: --");
        conditionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Forecast area
        forecastArea = new JTextArea(5, 30);
        forecastArea.setEditable(false);
        forecastArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Unit selector
        unitSelector = new JComboBox<>(new String[]{"Celsius / m/s", "Fahrenheit / km/h"});
        
        // History list
        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Main panel
        mainPanel = new JPanel();
        mainPanel.setBackground(BackgroundManager.getCurrentBackgroundColor());
    }
    
    /**
     * Setup the layout of components
     */
    private void setupLayout() {
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Top panel - Input
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("City:"));
        topPanel.add(cityTextField);
        topPanel.add(fetchButton);
        topPanel.add(new JLabel("Units:"));
        topPanel.add(unitSelector);
        
        // Center panel - Weather display
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        
        // Weather info panel
        JPanel weatherInfoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        weatherInfoPanel.setOpaque(false);
        weatherInfoPanel.add(temperatureLabel);
        weatherInfoPanel.add(humidityLabel);
        weatherInfoPanel.add(windLabel);
        weatherInfoPanel.add(conditionLabel);
        
        // Icon and info panel
        JPanel iconAndInfoPanel = new JPanel(new BorderLayout(10, 10));
        iconAndInfoPanel.setOpaque(false);
        iconAndInfoPanel.add(iconLabel, BorderLayout.WEST);
        iconAndInfoPanel.add(weatherInfoPanel, BorderLayout.CENTER);
        
        centerPanel.add(iconAndInfoPanel, BorderLayout.NORTH);
        
        // Forecast panel
        JPanel forecastPanel = new JPanel(new BorderLayout(5, 5));
        forecastPanel.setOpaque(false);
        forecastPanel.add(new JLabel("5-Hour Forecast:"), BorderLayout.NORTH);
        JScrollPane forecastScroll = new JScrollPane(forecastArea);
        forecastScroll.setOpaque(false);
        forecastScroll.getViewport().setOpaque(false);
        forecastPanel.add(forecastScroll, BorderLayout.CENTER);
        
        centerPanel.add(forecastPanel, BorderLayout.CENTER);
        
        // Right panel - History
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setOpaque(false);
        rightPanel.add(new JLabel("Search History:"), BorderLayout.NORTH);
        JScrollPane historyScroll = new JScrollPane(historyList);
        historyScroll.setPreferredSize(new Dimension(200, 0));
        rightPanel.add(historyScroll, BorderLayout.CENTER);
        
        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add padding
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(mainPanel);
    }
    
    /**
     * Setup event handlers for interactive components
     */
    private void setupEventHandlers() {
        // Fetch button action
        fetchButton.addActionListener(this::onFetchWeather);
        
        // Enter key in text field
        cityTextField.addActionListener(this::onFetchWeather);
        
        // Unit selector change
        unitSelector.addActionListener(e -> {
            useFahrenheit = unitSelector.getSelectedIndex() == 1;
            if (currentWeatherData != null) {
                updateWeatherDisplay();
            }
        });
        
        // History list selection
        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = historyList.getSelectedValue();
                if (selected != null) {
                    // Extract city name from history entry
                    String cityName = selected.split(" - ")[0];
                    cityTextField.setText(cityName);
                }
            }
        });
    }
    
    /**
     * Handle fetch weather button click
     */
    private void onFetchWeather(ActionEvent e) {
        String cityName = cityTextField.getText().trim();
        
        // Validate input
        if (cityName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a city name", 
                "Empty Input", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show loading state
        fetchButton.setEnabled(false);
        fetchButton.setText("Fetching...");
        
        // Fetch weather in background thread
        SwingWorker<WeatherData, Void> worker = new SwingWorker<WeatherData, Void>() {
            @Override
            protected WeatherData doInBackground() throws Exception {
                return WeatherAPI.fetchWeatherData(cityName);
            }
            
            @Override
            protected void done() {
                try {
                    currentWeatherData = get();
                    updateWeatherDisplay();
                    searchHistory.addSearch(cityName);
                    updateHistoryDisplay();
                    
                } catch (Exception ex) {
                    handleError(ex);
                } finally {
                    fetchButton.setEnabled(true);
                    fetchButton.setText("Fetch Weather");
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Update the weather display with current data
     */
    private void updateWeatherDisplay() {
        if (currentWeatherData == null) {
            return;
        }
        
        // Update labels
        temperatureLabel.setText("Temperature: " + 
            UnitConverter.formatTemperature(currentWeatherData.getTemperature(), useFahrenheit));
        
        humidityLabel.setText(String.format("Humidity: %.0f%%", currentWeatherData.getHumidity()));
        
        windLabel.setText("Wind Speed: " + 
            UnitConverter.formatWindSpeed(currentWeatherData.getWindSpeed(), useFahrenheit));
        
        String condition = WeatherAPI.getWeatherDescription(currentWeatherData.getWeatherCode());
        conditionLabel.setText("Condition: " + condition);
        
        // Update icon
        String iconName = WeatherAPI.getWeatherIcon(currentWeatherData.getWeatherCode());
        loadIcon(iconName);
        
        // Update forecast
        updateForecastDisplay();
    }
    
    /**
     * Update the forecast display
     */
    private void updateForecastDisplay() {
        if (currentWeatherData == null || currentWeatherData.getForecast() == null) {
            forecastArea.setText("No forecast available");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Time      Temp       Condition\n");
        sb.append("─────────────────────────────────────\n");
        
        for (WeatherData.ForecastEntry entry : currentWeatherData.getForecast()) {
            String temp = useFahrenheit ? 
                String.format("%.1f°F", UnitConverter.celsiusToFahrenheit(entry.getTemperature())) :
                String.format("%.1f°C", entry.getTemperature());
            
            String condition = WeatherAPI.getWeatherDescription(entry.getWeatherCode());
            
            sb.append(String.format("%-10s%-11s%s\n", 
                entry.getTime(), temp, condition));
        }
        
        forecastArea.setText(sb.toString());
    }
    
    /**
     * Update the search history display
     */
    private void updateHistoryDisplay() {
        historyListModel.clear();
        String[] history = searchHistory.getFormattedHistory();
        for (String entry : history) {
            historyListModel.addElement(entry);
        }
    }
    
    /**
     * Load and display weather icon
     */
    private void loadIcon(String iconName) {
        try {
            // Try to load from resources
            InputStream is = getClass().getResourceAsStream("/icons/" + iconName);
            if (is != null) {
                try {
                    Image img = ImageIO.read(is);
                    Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    iconLabel.setIcon(new ImageIcon(scaledImg));
                    return;
                } finally {
                    is.close();
                }
            }
            
            // Try to load from file system
            File iconFile = new File("src/main/resources/icons/" + iconName);
            if (iconFile.exists()) {
                Image img = ImageIO.read(iconFile);
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(scaledImg));
                return;
            }
            
            // Set placeholder text if icon not found
            iconLabel.setIcon(null);
            iconLabel.setText("[" + iconName + "]");
            
        } catch (Exception e) {
            iconLabel.setIcon(null);
            iconLabel.setText("[Icon]");
        }
    }
    
    /**
     * Handle errors during weather fetching
     */
    private void handleError(Exception ex) {
        String message;
        String title;
        
        if (ex.getMessage() != null && ex.getMessage().contains("City not found")) {
            message = "City not found. Please check the spelling and try again.";
            title = "Invalid City";
        } else if (ex.getMessage() != null && ex.getMessage().contains("HTTP request failed")) {
            message = "Network error. Please check your internet connection.";
            title = "Network Error";
        } else if (ex.getMessage() != null && ex.getMessage().contains("Invalid JSON")) {
            message = "Error parsing weather data. Please try again.";
            title = "Parse Error";
        } else {
            message = "An error occurred: " + ex.getMessage();
            title = "Error";
        }
        
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Update background color based on time of day
     */
    private void updateBackground() {
        Color bgColor = BackgroundManager.getCurrentBackgroundColor();
        mainPanel.setBackground(bgColor);
        
        // Adjust text color for dark backgrounds
        if (BackgroundManager.getCurrentTimePeriod().equals("Night")) {
            temperatureLabel.setForeground(Color.WHITE);
            humidityLabel.setForeground(Color.WHITE);
            windLabel.setForeground(Color.WHITE);
            conditionLabel.setForeground(Color.WHITE);
        } else {
            temperatureLabel.setForeground(Color.BLACK);
            humidityLabel.setForeground(Color.BLACK);
            windLabel.setForeground(Color.BLACK);
            conditionLabel.setForeground(Color.BLACK);
        }
    }
}
