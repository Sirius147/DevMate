FROM ubuntu:latest
LABEL authors="user"

ENTRYPOINT ["top", "-b"]

# ---- Build stage ----
FROM gradle:8.8-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle clean build -x test

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/build/libs/*SNAPSHOT*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]