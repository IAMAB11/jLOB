# Multi-stage build approach
# Build locally with: gradle bookJar -x test -x generateJLobJooqSchemaSource
# Then build the Docker image

FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy the pre-built JAR from build directory
# Assumes the JAR was built locally before docker build
COPY build/book.jar ./book.jar

# Copy configuration and resources
COPY src/main/resources ./resources

# Expose HTTP and FIX ports
EXPOSE 4567 9877

# Run the application
CMD ["java", "-jar", "book.jar", "resources/config.yaml"]
