FROM gradle:8.11-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle :backend:main-api:buildFatJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/backend/main-api/build/libs/*-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
