# Use the official OpenJDK image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 8080

# Command to run the JAR file
CMD ["java", "-jar", "demo.jar"]
