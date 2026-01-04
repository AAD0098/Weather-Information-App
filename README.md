# Weather Information App

A Java Swing desktop application that provides real-time weather information using the Open Meteo API. The app features a user-friendly interface with weather icons, unit conversion, search history, and dynamic time-based backgrounds.

## Features

### Core Features
- **Real-time Weather Data**: Fetches current weather information for any city worldwide
- **Weather Details**: Displays temperature, humidity, wind speed, and weather conditions
- **5-Hour Forecast**: Shows upcoming hourly weather predictions
- **Weather Icons**: Visual representation of weather conditions (clear, cloudy, rain, storm)

### Advanced Features
- **Unit Conversion**: Switch between Celsius/Fahrenheit and m/s/km/h for temperature and wind speed
- **Search History**: Automatically tracks your last 10 searches with timestamps
- **Dynamic Backgrounds**: Changes background color based on time of day (morning, afternoon, evening, night)
- **Error Handling**: Comprehensive error messages for invalid cities, network errors, and data parsing issues
- **Clickable History**: Click on any previous search to quickly re-search that city

## Requirements

- Java Development Kit (JDK) 8 or higher
- Internet connection (to fetch weather data from Open Meteo API)

## Project Structure

```
Weather-Information-App/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── weather/
│       │           └── app/
│       │               ├── Main.java                    # Application entry point
│       │               ├── api/
│       │               │   └── WeatherAPI.java          # Open Meteo API integration
│       │               ├── model/
│       │               │   └── WeatherData.java         # Weather data model
│       │               ├── ui/
│       │               │   └── WeatherGUI.java          # Main GUI interface
│       │               └── util/
│       │                   ├── BackgroundManager.java   # Dynamic backgrounds
│       │                   ├── SearchHistory.java       # Search history management
│       │                   └── UnitConverter.java       # Unit conversion utilities
│       └── resources/
│           └── icons/                                   # Weather icon images
│               ├── clear.png
│               ├── cloudy.png
│               ├── rain.png
│               └── storm.png
└── README.md
```

## How to Build and Run

### Using Command Line

1. **Clone the repository** (if not already done):
   ```bash
   git clone https://github.com/AAD0098/Weather-Information-App.git
   cd Weather-Information-App
   ```

2. **Compile the Java files**:
   ```bash
   javac -d bin -sourcepath src/main/java src/main/java/com/weather/app/Main.java
   ```

3. **Run the application**:
   ```bash
   java -cp bin com.weather.app.Main
   ```

### Using an IDE (Eclipse, IntelliJ IDEA, NetBeans)

1. **Import the project**:
   - Open your IDE
   - Select "Import" or "Open Project"
   - Navigate to the project directory and select it

2. **Configure the project**:
   - Ensure JDK 8+ is configured as the project SDK
   - Mark `src/main/java` as the source root
   - Mark `src/main/resources` as the resources root

3. **Run the application**:
   - Locate `Main.java` in the project explorer
   - Right-click and select "Run" or "Run As Java Application"

## How to Use

1. **Start the Application**: Launch the app using one of the methods above

2. **Search for Weather**:
   - Enter a city name in the text field (e.g., "London", "New York", "Tokyo")
   - Click "Fetch Weather" button or press Enter
   - Wait for the weather data to load

3. **View Weather Information**:
   - Current temperature, humidity, and wind speed
   - Weather condition description
   - Weather icon representing current conditions
   - 5-hour forecast in the forecast area

4. **Change Units**:
   - Use the "Units" dropdown to switch between:
     - Celsius / m/s (metric)
     - Fahrenheit / km/h (imperial)
   - The display updates automatically

5. **View Search History**:
   - All successful searches appear in the right panel
   - Shows city name and timestamp
   - Click any history entry to search that city again

6. **Dynamic Background**:
   - The background color changes automatically based on time:
     - Morning (6:00-12:00): Light orange
     - Afternoon (12:00-18:00): Light sky blue
     - Evening (18:00-21:00): Light pink
     - Night (21:00-6:00): Midnight blue

## API Information

This app uses the [Open Meteo API](https://open-meteo.com/), which provides:
- Free weather data with no API key required
- Global coverage for cities worldwide
- Current weather conditions and forecasts
- Weather codes for condition classification

## Error Handling

The app handles various error scenarios:
- **Empty City Input**: Warns when no city name is entered
- **Invalid City**: Alerts when the city cannot be found
- **Network Errors**: Notifies when internet connection fails
- **Parsing Errors**: Handles malformed API responses gracefully

## Weather Code Mapping

Weather conditions are determined by Open Meteo weather codes:
- **0**: Clear sky
- **1-3**: Partly cloudy
- **45-48**: Foggy
- **51-67**: Rainy
- **71-77**: Snowy
- **80-82**: Rain showers
- **85-86**: Snow showers
- **95-99**: Thunderstorm

## Customization

### Adding Custom Icons
Replace the placeholder icons in `src/main/resources/icons/` with your own 100x100 PNG images:
- `clear.png` - Clear sky conditions
- `cloudy.png` - Cloudy or partly cloudy
- `rain.png` - Rain, drizzle, or snow
- `storm.png` - Thunderstorms

### Modifying Background Colors
Edit `BackgroundManager.java` to customize the time periods and colors:
```java
private static final Color MORNING_COLOR = new Color(255, 223, 186);
private static final Color AFTERNOON_COLOR = new Color(135, 206, 250);
// etc.
```

### Changing History Size
Modify the `MAX_HISTORY_SIZE` constant in `SearchHistory.java`:
```java
private static final int MAX_HISTORY_SIZE = 10;  // Change this value
```

## Troubleshooting

**Problem**: "City not found" error
- **Solution**: Check the spelling of the city name. Try using the full city name or include the country (e.g., "Paris, France")

**Problem**: Network error messages
- **Solution**: Verify your internet connection is active and stable

**Problem**: Icons not displaying
- **Solution**: Ensure the icons folder exists at `src/main/resources/icons/` with all required PNG files

**Problem**: App window too small/large
- **Solution**: Modify the window size in `WeatherGUI.java`:
  ```java
  setSize(800, 600);  // Change width and height as needed
  ```

## Future Enhancements

Potential features for future versions:
- Multi-day forecast (3-day, 7-day)
- Weather alerts and warnings
- Multiple location comparison
- Save favorite locations
- Temperature graphs and charts
- Export weather data to CSV
- Dark mode toggle
- Multiple language support

## Credits

- **Weather Data**: [Open Meteo API](https://open-meteo.com/)
- **Geocoding**: Open Meteo Geocoding API
- **Icons**: Placeholder icons included (replace with custom icons)

## License

This project is created for educational purposes. Feel free to use and modify as needed.

## Author

AAD0098

## Version

1.0.0 - Initial Release
