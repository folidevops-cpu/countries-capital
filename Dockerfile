# Single-stage build for Spring Boot application
FROM amazoncorretto:8-alpine3.19-jre

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven curl && \
    rm -rf /var/lib/apt/lists/*

# Copy Maven configuration and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Change ownership of the app directory to spring user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose the application port
EXPOSE 8080

# Configure JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Health check using actuator endpoint
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/country-capital-1.0.0.jar"]
