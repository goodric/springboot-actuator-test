#!/bin/bash

# Get the absolute path of the current directory
CURRENT_DIR=$(pwd)
LOG_DIR="$CURRENT_DIR/logs"

echo "Creating log directory at: $LOG_DIR"
mkdir -p "$LOG_DIR"

# Verify directory creation
if [ -d "$LOG_DIR" ]; then
    echo "Log directory created successfully"
    ls -la "$LOG_DIR"
else
    echo "Failed to create log directory"
    exit 1
fi

# Start the application with explicit log file path
echo "Starting application..."
java -jar target/spring-boot-test-1.0-SNAPSHOT.jar --logging.file.path="$LOG_DIR" --logging.file.name="$LOG_DIR/application.log" 