# Use the official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application (expecting an external .jar)
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
