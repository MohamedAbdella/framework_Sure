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
ANDROID_EMULATOR_ID=$(get_property "androidEmulatorId")
echo "Starting Android Emulator ID: $ANDROID_EMULATOR_ID"

# Close Android emulator Id
adb -s "$ANDROID_EMULATOR_ID" emu kill
