# Use a lightweight Java runtime
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled executable jar from your target folder into the container
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]