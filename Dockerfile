# ─── Build stage (JDK 21 + Gradle wrapper) ─────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# copy everything (including your gradlew + wrapper jars)
COPY . .

# make the wrapper executable and build your fat-jar
RUN chmod +x gradlew \
 && ./gradlew bootJar --no-daemon --stacktrace

# ─── Runtime stage (JRE 21) ────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# pull the jar from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
