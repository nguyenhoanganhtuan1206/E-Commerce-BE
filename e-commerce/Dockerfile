# Use the desired base image with Java 17
FROM eclipse-temurin:17-jdk-focal

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper files
COPY gradle/ gradle/
COPY gradlew .

# Copy the Gradle project descriptor and properties files
COPY build.gradle .
COPY settings.gradle .

# Copy the source code into the container
COPY src ./src

# Run the Gradle build
RUN ./gradlew clean build

# Specify the command to run your Spring Boot application
CMD ["./gradlew", "bootRun"]
