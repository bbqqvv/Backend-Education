# Dockerfile

# Base image
FROM eclipse-temurin:17-jdk-alpine

# App directory in container
WORKDIR /app

# Copy built jar file (nhớ build trước)
COPY target/*.jar app.jar

# App port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
