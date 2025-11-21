FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Build application
RUN ./mvnw clean package -DskipTests


# ---------------- RUNTIME ----------------
FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/GPA_CGPACALC-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
