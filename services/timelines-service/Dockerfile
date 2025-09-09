# Stage 1: Build the application
FROM openjdk:21-jdk-slim AS build
WORKDIR /app

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven Wrapper files and the pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Grant execute permissions to the Maven Wrapper script
RUN chmod +x mvnw

# Copy the source code
COPY src src

# Build the application using the Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]