# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Pre-download dependencies (tests disabled)
RUN ./mvnw -Dmaven.test.skip=true dependency:go-offline || mvn -Dmaven.test.skip=true dependency:go-offline

# Copy source code
COPY src ./src

# Build jar (no tests compiled or run)
RUN ./mvnw -Dmaven.test.skip=true package || mvn -Dmaven.test.skip=true package


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

# Environment variables (no "=")
ENV DB_URL \
    DB_USERNAME \
    DB_PASSWORD \
    JWT_SECRET

# Expose port
EXPOSE 8081

# Create non-root user
RUN useradd -m spring
USER spring

WORKDIR /app

# Copy final jar from build stage
COPY --chown=spring:spring --from=build /app/target/*.jar /app/app.jar

# Run application on port 8081
ENTRYPOINT ["java", "-Dserver.port=8081", "-jar", "/app/app.jar"]
