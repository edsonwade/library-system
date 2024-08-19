# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application jar file into the container
COPY target/library-management-system.jar /app/library-management-system.jar

# Expose the application's port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app/library-management-system.jar"]
