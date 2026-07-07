# ---- Stage 1: build the jar ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Cache dependencies first (only re-downloads when pom.xml changes)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Build the application
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Stage 2: slim runtime image ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
