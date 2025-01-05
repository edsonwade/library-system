#!/bin/bash

# Run the Maven clean install
mvn clean package

# Check if Maven build was successful
if [ $? -eq 0 ]; then
    # If successful, proceed with the Docker build
    echo "Maven build successful. Proceeding with Docker build..."
    docker build -t library-management-system:latest .
else
    # If Maven build failed, print an error and exit
    echo "Maven build failed. Aborting Docker build."
    exit 1
fi
