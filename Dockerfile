# Use OpenJDK 17 as the base image (matching your Spring Boot 3.2.0 requirement)
FROM openjdk:17-jdk-slim
# Set metadata
LABEL maintainer="folidevops"
LABEL description="Country Capital & Task Manager Spring Boot Application"
LABEL version="1.0.0"

# Create a non-root user for security

#RUN addgroup -g 1001 -S appgroup && \
    #adduser -u 1001 -S appuser -G appgroup

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
# Using the specific JAR name for better clarity
COPY target/*.jar app.jar

# Change ownership of the app directory to the non-root user

#RUN chown -R appuser:appgroup /app

# Switch to non-root user

#USER appuser

# Expose the port that Spring Boot uses (default is 8080)
EXPOSE 8080

# Set JVM options optimized for containers and Spring Boot 3.x
ENV JAVA_OPTS="-Xmx768m -Xms512m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Health check to ensure the application is running
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]