# Weather Information App

A Java-based desktop application that provides real-time weather information using the free Open-Meteo API. The app features a simple and intuitive Swing GUI with weather icons, forecasts, unit conversion, search history, and dynamic backgrounds.

## Features

### ✨ Core Features
- **Real-time Weather Data**: Fetches current weather information from the Open-Meteo API
- **City Search**: Enter any city name to get weather information
- **Weather Display**: Shows temperature, humidity, wind speed, and weather conditions
- **Weather Icons**: Unicode-based weather icons for different conditions (☀, 🌧, ❄, ⛈, etc.)
- **5-Day Forecast**: Short-term forecast with daily high/low temperatures and conditions

### 🎨 User Experience
- **Unit Conversion**: Toggle between Celsius/Fahrenheit and km/h/mph
- **Search History**: Keeps track of recently searched cities for quick access
- **Dynamic Backgrounds**: Time-based background colors that change throughout the day
  - Dawn (5-7): Orange/Yellow
  - Morning (7-12): Light Sky Blue
  - Afternoon (12-17): Steel Blue
  - Evening (17-19): Dark Orange
  - Night (19-5): Midnight Blue

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Internet connection (for API calls)

## Building the Application

1. Clone the repository:
```bash
git clone https://github.com/AAD0098/Weather-Information-App.git
cd Weather-Information-App
```

2. Build with Maven:
```bash
mvn clean package
```

This will create an executable JAR file in the `target` directory.

## Running the Application

Execute the JAR file:
```bash
java -jar target/weather-information-app-1.0.0.jar
```

Or run directly with Maven:
```bash
mvn exec:java -Dexec.mainClass="com.weather.app.WeatherApp"
```

## Usage

1. **Search for a City**: Enter a city name in the search field and click "Search" or press Enter
2. **View Weather**: The current weather will display with an icon, temperature, humidity, and wind speed
3. **Check Forecast**: View the 5-day forecast at the bottom of the window
4. **Toggle Units**: Click the °C/°F button to switch between temperature units
5. **Use History**: Click the dropdown menu to quickly access recently searched cities

## Project Structure

```
Weather-Information-App/
├── src/main/java/com/weather/app/
│   ├── WeatherApp.java          # Main GUI application
│   ├── WeatherAPI.java           # API integration with Open-Meteo
│   ├── WeatherData.java          # Weather data model
│   ├── ForecastData.java         # Forecast data model
│   ├── WeatherIcon.java          # Weather icon utilities
│   ├── UnitConverter.java        # Temperature and speed conversion
│   ├── SearchHistory.java        # Search history management
│   └── TimeBasedBackground.java  # Dynamic background logic
├── pom.xml                       # Maven configuration
└── README.md                     # This file
```

## Technologies Used

- **Java 11**: Core programming language
- **Java Swing**: GUI framework
- **Open-Meteo API**: Free weather API (no API key required)
- **JSON Library**: For parsing API responses (org.json)
- **Maven**: Build and dependency management

## API Information

This application uses the [Open-Meteo API](https://open-meteo.com/), which is:
- Free and open-source
- No API key required
- No authentication needed
- Provides accurate weather forecasts

## Features in Detail

### Weather Conditions Supported
- Clear Sky (☀)
- Partly Cloudy (⛅)
- Fog (🌫)
- Rain (🌧)
- Snow (❄)
- Thunderstorm (⛈)
- And more...

### Unit Conversion
- Temperature: Celsius ↔ Fahrenheit
- Wind Speed: km/h ↔ mph

### Search History
- Stores up to 10 recent searches
- Quick access via dropdown menu
- Persistent during application session

## License

This project is open source and available under the MIT License.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.

## Author

AAD0098

## Acknowledgments

- Weather data provided by [Open-Meteo API](https://open-meteo.com/)
- Icons use Unicode emoji symbols for cross-platform compatibility
