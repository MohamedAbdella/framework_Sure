#!/bin/bash

# Define the properties file
CONFIG_FILE="$(dirname "$0")/properties/mobile.properties"
echo "CONFIG_FILE: $CONFIG_FILE"

# Function to read properties from the configuration file
function get_property {
  PROP_KEY=$1
  PROP_VALUE=$(grep "${PROP_KEY}" "${CONFIG_FILE}" | cut -d'=' -f2)
  echo "${PROP_VALUE}"
}

# Get the properties from the properties file
ANDROID_DEVICE_NAME=$(get_property "androidDeviceName")

# Ensure your Android SDK path is set correctly
ANDROID_HOME=~/Library/Android/sdk
EMULATOR=$ANDROID_HOME/emulator/emulator
ADB=$ANDROID_HOME/platform-tools/adb

# Start the Android emulator
echo "Starting Android Emulator: $ANDROID_DEVICE_NAME"
$EMULATOR -avd "$ANDROID_DEVICE_NAME" &

# Wait for the emulator to start (adjust the sleep duration as needed)
sleep 30

# Check if the emulator started successfully
EMULATOR_RUNNING=$($ADB devices | grep -c emulator)

if [ "$EMULATOR_RUNNING" -eq 0 ]; then
  echo "Failed to start the emulator."
else
  echo "Emulator started successfully."
fi
