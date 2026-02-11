# Build stage
FROM gradle:7.6-jdk11 AS build
WORKDIR /app

# Copy gradle and source files
COPY build.gradle .
COPY src src

# Build the JAR (skip tests and JOOQ generation for now as they require DB)
RUN gradle bookJar -x test -x generateJLobJooqSchemaSource

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/build/book.jar ./book.jar

# Copy configuration and resources
COPY src/main/resources ./resources

# Expose HTTP and FIX ports
EXPOSE 4567 9877

# Run the application
CMD ["java", "-jar", "book.jar", "resources/config.yaml"]
