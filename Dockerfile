# Use Amazon Corretto 8 as the base image
FROM amazoncorretto:8-alpine3.19-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/*.jar app.jar

# Expose the port that Spring Boot uses (default is 8080)
EXPOSE 8080

# Set JVM options for better container performance
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]