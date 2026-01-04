#!/bin/bash

# Build script for Weather Information App

echo "Building Weather Information App..."
echo "=================================="

# Create bin directory
mkdir -p bin

# Compile Java files
echo "Compiling Java files..."
javac -d bin -sourcepath src/main/java src/main/java/com/weather/app/Main.java

if [ $? -eq 0 ]; then
    echo "✓ Build successful!"
    echo ""
    echo "To run the application, execute:"
    echo "  java -cp bin com.weather.app.Main"
else
    echo "✗ Build failed!"
    exit 1
fi
