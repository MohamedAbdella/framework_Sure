#!/bin/bash

# Step 1: Generate the Allure report


# Step 3: Serve the Allure report
echo "Serving Allure report..."
allure serve &

# Wait a few seconds to ensure the server starts
sleep 5
# Exit the script
exit
