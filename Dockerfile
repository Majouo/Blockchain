FROM ubuntu:latest
LABEL authors="Majouo"

# Use a base image with a Java runtime
FROM openjdk:21-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container
COPY out/artifacts/Blockchain_jar/Blockchain.jar /app/

# Command to run your application
CMD ["java", "-jar", "Blockchain.jar"]