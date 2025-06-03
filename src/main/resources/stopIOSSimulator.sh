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
IOS_DEVICE_NAME=$(get_property "iosDeviceName")
DEVICE_UDID=$(xcrun simctl list devices | grep "$IOS_DEVICE_NAME (" | awk -F '[()]' '{print $2}')

if [ -n "$DEVICE_UDID" ]; then
  echo "Shutting down device $IOS_DEVICE_NAME with UDID $DEVICE_UDID"
  xcrun simctl shutdown "$DEVICE_UDID"
fi

# Kill the Simulator app completely
echo "Terminating the Simulator app."
osascript -e 'tell application "Simulator" to quit'
