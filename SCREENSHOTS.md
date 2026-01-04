# Screenshot Guide for Weather Information App

This guide explains what screenshots to take to demonstrate the application's features.

## Required Screenshots

### 1. Main GUI (Empty State)
**Filename**: `screenshot_01_main_gui.png`
- Launch the application
- Show the empty state before any weather search
- Capture the full window including:
  - City input field
  - Fetch Weather button
  - Unit selector dropdown
  - Empty weather display area
  - Empty forecast area
  - Empty history list

### 2. Weather Results (Valid City)
**Filename**: `screenshot_02_weather_results.png`
- Enter a valid city name (e.g., "London", "New York", "Tokyo")
- Click "Fetch Weather" button
- Capture the window showing:
  - Populated temperature, humidity, wind speed
  - Weather condition text
  - Weather icon
  - 5-hour forecast data
  - Updated search history with timestamp

### 3. Unit Conversion (Fahrenheit)
**Filename**: `screenshot_03_fahrenheit.png`
- With weather data displayed
- Change the unit selector to "Fahrenheit / km/h"
- Capture the updated display showing:
  - Temperature in Fahrenheit
  - Wind speed in km/h
  - Forecast temperatures in Fahrenheit

### 4. Search History
**Filename**: `screenshot_04_history.png`
- Search for multiple cities (3-5 different cities)
- Capture the window showing:
  - Multiple entries in the history list
  - Each entry with city name and timestamp
  - Highlighted/selected history entry

### 5. Error Handling (Invalid City)
**Filename**: `screenshot_05_invalid_city.png`
- Enter an invalid or non-existent city name
- Click "Fetch Weather" button
- Capture the error dialog showing:
  - Error message: "City not found"
  - Dialog with appropriate error text

### 6. Error Handling (Empty Input)
**Filename**: `screenshot_06_empty_input.png`
- Leave the city field empty
- Click "Fetch Weather" button
- Capture the warning dialog showing:
  - Warning message: "Please enter a city name"

### 7. Dynamic Background - Morning
**Filename**: `screenshot_07_morning_background.png`
- Launch app during morning hours (6:00-12:00)
- OR modify system time if possible
- Capture window showing light orange background

### 8. Dynamic Background - Afternoon
**Filename**: `screenshot_08_afternoon_background.png`
- Run app during afternoon hours (12:00-18:00)
- Capture window showing light blue background

### 9. Dynamic Background - Evening
**Filename**: `screenshot_09_evening_background.png`
- Run app during evening hours (18:00-21:00)
- Capture window showing light pink background

### 10. Dynamic Background - Night
**Filename**: `screenshot_10_night_background.png`
- Run app during night hours (21:00-6:00)
- Capture window showing dark blue background
- Note: Text should be white on dark background

## How to Take Screenshots

### Windows
1. Click on the Weather App window to focus it
2. Press `Alt + Print Screen` to capture the active window
3. Open Paint or another image editor
4. Press `Ctrl + V` to paste
5. Save with the appropriate filename

### macOS
1. Click on the Weather App window to focus it
2. Press `Cmd + Shift + 4`, then press `Space`
3. Click on the window to capture it
4. The screenshot will be saved to your desktop
5. Rename to the appropriate filename

### Linux
1. Use the built-in screenshot tool or `gnome-screenshot`
2. Select "Capture window" option
3. Click on the Weather App window
4. Save with the appropriate filename

## Screenshot Organization

Create a `screenshots/` folder in the project root and save all screenshots there:

```
Weather-Information-App/
├── screenshots/
│   ├── screenshot_01_main_gui.png
│   ├── screenshot_02_weather_results.png
│   ├── screenshot_03_fahrenheit.png
│   ├── screenshot_04_history.png
│   ├── screenshot_05_invalid_city.png
│   ├── screenshot_06_empty_input.png
│   ├── screenshot_07_morning_background.png
│   ├── screenshot_08_afternoon_background.png
│   ├── screenshot_09_evening_background.png
│   └── screenshot_10_night_background.png
└── ...
```

## Tips for Good Screenshots

1. **Clean Desktop**: Close unnecessary applications before taking screenshots
2. **Full Window**: Ensure the entire application window is visible
3. **Readable Text**: Make sure all text is legible in the screenshot
4. **Proper Timing**: Wait for all data to load before capturing
5. **Consistent Size**: Try to keep the window size consistent across screenshots
6. **High Quality**: Save in PNG format for best quality

## Including in Documentation

Add screenshots to your README or submission document with captions:

```markdown
## Application Screenshots

### Main Interface
![Main GUI](screenshots/screenshot_01_main_gui.png)
*The main interface showing input field, unit selector, and display areas*

### Weather Results
![Weather Results](screenshots/screenshot_02_weather_results.png)
*Weather information displayed for a city, including forecast and history*

### Unit Conversion
![Fahrenheit Display](screenshots/screenshot_03_fahrenheit.png)
*Temperature and wind speed shown in Fahrenheit and km/h*

... and so on
```
