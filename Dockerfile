
# Build stage
FROM eclipse-temurin:21-jdk as build
WORKDIR /app
# If using Maven Wrapper, ensure execute bit
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -q -B -e -DskipTests dependency:go-offline
COPY src ./src
RUN ./mvnw -q -B -DskipTests package

# Runtime stage (JRE is enough)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
