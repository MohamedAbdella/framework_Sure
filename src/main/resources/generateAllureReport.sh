#!/bin/bash

# Step 1: Run your tests
echo "Running tests..."
mvn clean test

# Step 2: Generate the Allure report
echo "Generating Allure report..."
allure generate --clean

# Step 3: Serve the Allure report
echo "Serving Allure report..."
allure serve &

# Wait a few seconds to ensure the server starts
sleep 5
# Exit the script
exit
