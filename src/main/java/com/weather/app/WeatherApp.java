package com.weather.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main GUI application for Weather Information App
 */
public class WeatherApp extends JFrame {
    private WeatherAPI weatherAPI;
    private SearchHistory searchHistory;
    private boolean useCelsius = true;

    // UI Components
    private JTextField cityTextField;
    private JComboBox<String> historyComboBox;
    private JButton searchButton;
    private JToggleButton unitToggleButton;
    private JPanel mainPanel;
    private JPanel weatherDisplayPanel;
    private JPanel forecastPanel;
    private JLabel statusLabel;

    // Current weather data
    private WeatherData currentWeatherData;
    private List<ForecastData> currentForecast;

    public WeatherApp() {
        weatherAPI = new WeatherAPI();
        searchHistory = new SearchHistory();

        setTitle("Weather Information App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initializeUI();
        applyTimeBasedBackground();

        // Update background every minute
        Timer backgroundTimer = new Timer(60000, e -> applyTimeBasedBackground());
        backgroundTimer.start();
    }

    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel - Search and controls
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel - Weather display
        weatherDisplayPanel = new JPanel(new BorderLayout());
        weatherDisplayPanel.setBorder(BorderFactory.createTitledBorder("Current Weather"));
        JLabel welcomeLabel = new JLabel("Enter a city name to get weather information", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        weatherDisplayPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(weatherDisplayPanel, BorderLayout.CENTER);

        // Bottom panel - Forecast
        forecastPanel = new JPanel();
        forecastPanel.setBorder(BorderFactory.createTitledBorder("5-Day Forecast"));
        mainPanel.add(forecastPanel, BorderLayout.SOUTH);

        // Status bar
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(statusLabel, BorderLayout.PAGE_END);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("City:");
        cityTextField = new JTextField(20);
        searchButton = new JButton("Search");

        // History combo box
        historyComboBox = new JComboBox<>();
        historyComboBox.addItem("-- Recent Searches --");
        historyComboBox.setPreferredSize(new Dimension(150, 25));

        // Unit toggle button
        unitToggleButton = new JToggleButton("°C", true);
        unitToggleButton.setToolTipText("Toggle between Celsius and Fahrenheit");

        searchPanel.add(searchLabel);
        searchPanel.add(cityTextField);
        searchPanel.add(searchButton);
        searchPanel.add(historyComboBox);
        searchPanel.add(unitToggleButton);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Add action listeners
        searchButton.addActionListener(e -> searchWeather());
        cityTextField.addActionListener(e -> searchWeather());

        historyComboBox.addActionListener(e -> {
            if (historyComboBox.getSelectedIndex() > 0) {
                String selectedCity = (String) historyComboBox.getSelectedItem();
                cityTextField.setText(selectedCity);
                searchWeather();
            }
        });

        unitToggleButton.addActionListener(e -> {
            useCelsius = !useCelsius;
            unitToggleButton.setText(useCelsius ? "°C" : "°F");
            // Refresh display with new units
            if (currentWeatherData != null) {
                displayWeatherData(currentWeatherData);
            }
            if (currentForecast != null) {
                displayForecast(currentForecast);
            }
        });

        return topPanel;
    }

    private void searchWeather() {
        String city = cityTextField.getText().trim();
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        statusLabel.setText("Fetching weather data for " + city + "...");
        searchButton.setEnabled(false);
        cityTextField.setEnabled(false);

        // Fetch weather data in background thread
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private WeatherData weatherData;
            private List<ForecastData> forecast;
            private Exception error;

            @Override
            protected Void doInBackground() {
                try {
                    weatherData = weatherAPI.getWeatherData(city);
                    forecast = weatherAPI.getForecastData(city);
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void done() {
                searchButton.setEnabled(true);
                cityTextField.setEnabled(true);

                if (error != null) {
                    statusLabel.setText("Error: " + error.getMessage());
                    JOptionPane.showMessageDialog(WeatherApp.this,
                            "Failed to fetch weather data: " + error.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    currentWeatherData = weatherData;
                    currentForecast = forecast;
                    displayWeatherData(weatherData);
                    displayForecast(forecast);
                    searchHistory.addCity(weatherData.getCityName());
                    updateHistoryComboBox();
                    statusLabel.setText("Weather data updated successfully");
                }
            }
        };

        worker.execute();
    }

    private void displayWeatherData(WeatherData data) {
        weatherDisplayPanel.removeAll();

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // City name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel cityLabel = new JLabel(data.getCityName());
        cityLabel.setFont(new Font("Arial", Font.BOLD, 28));
        contentPanel.add(cityLabel, gbc);

        // Weather icon
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel iconLabel = WeatherIcon.getIconLabel(data.getWeatherCode());
        contentPanel.add(iconLabel, gbc);

        // Weather info
        gbc.gridx = 1;
        gbc.gridy = 1;
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        String tempStr = UnitConverter.formatTemperature(data.getTemperature(), useCelsius);
        JLabel tempLabel = new JLabel(tempStr);
        tempLabel.setFont(new Font("Arial", Font.BOLD, 48));
        tempLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(tempLabel);

        JLabel conditionLabel = new JLabel(data.getCondition());
        conditionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        conditionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(conditionLabel);

        contentPanel.add(infoPanel, gbc);

        // Additional details
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        String windStr = UnitConverter.formatWindSpeed(data.getWindSpeed(), useCelsius);
        JLabel humidityLabel = new JLabel("💧 Humidity: " + data.getHumidity() + "%");
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel windLabel = new JLabel("💨 Wind: " + windStr);
        windLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        detailsPanel.add(humidityLabel);
        detailsPanel.add(windLabel);
        contentPanel.add(detailsPanel, gbc);

        weatherDisplayPanel.add(contentPanel, BorderLayout.CENTER);
        weatherDisplayPanel.revalidate();
        weatherDisplayPanel.repaint();
    }

    private void displayForecast(List<ForecastData> forecast) {
        forecastPanel.removeAll();
        forecastPanel.setLayout(new GridLayout(1, forecast.size(), 10, 10));

        for (ForecastData day : forecast) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JLabel dateLabel = new JLabel(day.getDate());
            dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel iconLabel = WeatherIcon.getSmallIconLabel(day.getWeatherCode());
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            String maxTempStr = UnitConverter.formatTemperature(day.getMaxTemp(), useCelsius);
            String minTempStr = UnitConverter.formatTemperature(day.getMinTemp(), useCelsius);
            JLabel tempLabel = new JLabel(maxTempStr + " / " + minTempStr);
            tempLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel condLabel = new JLabel(day.getCondition());
            condLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            condLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            dayPanel.add(dateLabel);
            dayPanel.add(Box.createVerticalStrut(5));
            dayPanel.add(iconLabel);
            dayPanel.add(Box.createVerticalStrut(5));
            dayPanel.add(tempLabel);
            dayPanel.add(condLabel);

            forecastPanel.add(dayPanel);
        }

        forecastPanel.revalidate();
        forecastPanel.repaint();
    }

    private void updateHistoryComboBox() {
        historyComboBox.removeAllItems();
        historyComboBox.addItem("-- Recent Searches --");

        List<String> history = searchHistory.getHistory();
        // Add in reverse order to show most recent first
        for (int i = history.size() - 1; i >= 0; i--) {
            historyComboBox.addItem(history.get(i));
        }
    }

    private void applyTimeBasedBackground() {
        Color bgColor = TimeBasedBackground.getBackgroundColor();
        Color textColor = TimeBasedBackground.getTextColor();

        mainPanel.setBackground(bgColor);
        weatherDisplayPanel.setBackground(bgColor);
        forecastPanel.setBackground(bgColor);

        // Set text colors
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                comp.setBackground(bgColor);
                setTextColorRecursive(comp, textColor);
            }
        }
    }

    private void setTextColorRecursive(Component comp, Color color) {
        if (comp instanceof JLabel) {
            comp.setForeground(color);
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setTextColorRecursive(child, color);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            WeatherApp app = new WeatherApp();
            app.setVisible(true);
        });
    }
}
