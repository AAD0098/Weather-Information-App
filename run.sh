#!/bin/bash

# Run script for Weather Information App

# Check if compiled
if [ ! -d "bin" ] || [ ! -f "bin/com/weather/app/Main.class" ]; then
    echo "Application not built. Building now..."
    ./build.sh
    if [ $? -ne 0 ]; then
        exit 1
    fi
fi

echo "Starting Weather Information App..."
java -cp bin com.weather.app.Main
