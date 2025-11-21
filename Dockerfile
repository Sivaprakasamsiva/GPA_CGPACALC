FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy everything to container
COPY . .

# Fix Linux permission issue (the real cause of the error)
RUN chmod +x mvnw

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# ---- RUNTIME IMAGE ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/target/GPA_CGPACALC-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
