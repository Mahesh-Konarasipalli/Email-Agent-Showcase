# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
# 🚀 FIX 1: Look for the .war file instead of .jar
COPY --from=build /app/target/*.war app.war 
EXPOSE 8080
# 🚀 FIX 2: Run the .war file
ENTRYPOINT ["java", "-Xmx300m", "-Xss512k", "-jar", "app.war"]