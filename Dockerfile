
# Build with Maven image (Temurin 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom first and warm the cache
COPY pom.xml .
RUN mvn -q -B -e -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src


# Runtime on JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
