# Use the official Gradle image as the base image
# FROM openjdk:17-alpine
FROM gradle:7.6.1-jdk17-alpine

# Set the working directory in the container
WORKDIR /code

# Copy the build.gradle and settings.gradle files into the container
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ ./gradle/
COPY launch.sh ./

# Copy the entire src directory into the container
COPY src/ ./src/

# Make the script executable
RUN chmod +x launch.sh

# Execute the script when the container starts
CMD ["./launch.sh"]