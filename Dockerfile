
# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom first to leverage build cache for dependencies
COPY pom.xml .
RUN mvn -q -B -e -DskipTests dependency:go-offline

# Now copy sources and build
COPY src ./src
# Builds Spring Boot fat jar (default)
RUN mvn -q -B -DskipTests package


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# NOTE: Running as root (per your request)
# No useradd, no USER command needed.

# Copy built jar
COPY --from=build /app/target/*.jar /app/app.jar

# Healthcheck (adjust if you don't use actuator)
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -fsS http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# JVM tuning
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+TieredCompilation -XX:TieredStopAtLevel=1"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
