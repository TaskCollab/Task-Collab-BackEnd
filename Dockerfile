# Use the official OpenJDK image as the base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy your JAR into the container at /app/app.jar
# (Make sure your final jar is named "app.jar" after build)
COPY target/app.jar /app/app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
