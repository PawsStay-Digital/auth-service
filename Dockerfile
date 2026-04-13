# Stage 1: Build stage
# Using Eclipse Temurin JDK 25 as the base image for compilation
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker cache for dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Grant execution permission to the Maven wrapper
RUN chmod +x mvnw

# Download project dependencies (this layer is cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime stage
# Using a lightweight JRE 25 image for the production environment
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy the compiled JAR file from the build stage
# Assuming the JAR name follows the standard Maven naming convention
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the Owner Service runs on
EXPOSE 8081

# Run the application with optimized settings for container environments
ENTRYPOINT ["java", "-jar", "app.jar"]