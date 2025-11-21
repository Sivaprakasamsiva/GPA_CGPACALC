FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy everything
COPY . .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build the jar
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built jar to final container
COPY --from=build /app/target/GPA_CGPACALC-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
