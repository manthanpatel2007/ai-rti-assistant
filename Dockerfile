# ================================
# Stage 1: Build
# ================================
FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy Maven configuration first
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -DskipTests

# Copy source code
COPY src src

# Build Spring Boot application
RUN ./mvnw clean package -DskipTests


# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy generated Spring Boot JAR
COPY --from=builder /app/target/*.jar app.jar

# Spring Boot port
EXPOSE 8080

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]