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
RUNTIME="com.apple.CoreSimulator.SimRuntime.iOS-$(get_property "iosPlatformVersion")-0"

if [ -z "$DEVICE_UDID" ]; then
  echo "Device $IOS_DEVICE_NAME not found. Creating a new simulator."
  DEVICE_UDID=$(xcrun simctl create "$IOS_DEVICE_NAME" com.apple.CoreSimulator.SimDeviceType."$IOS_DEVICE_NAME" "$RUNTIME")
fi

echo "Booting device $IOS_DEVICE_NAME with UDID $DEVICE_UDID"
xcrun simctl boot "$DEVICE_UDID"

echo "Opening simulator"
open -a Simulator
