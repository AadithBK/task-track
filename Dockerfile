# Stage 1: Build the application using a Maven or Gradle image
FROM maven:3.8.1-openjdk-17 AS MAVEN_BUILD
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final, lightweight runtime image
FROM openjdk:21
WORKDIR /app
COPY --from=MAVEN_BUILD target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
