
# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# 1) gradle wrapper와 빌드 스크립트 먼저 복사 -> 의존성 캐시 최적화
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/gradle-wrapper.jar
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.properties
COPY settings.gradle* build.gradle* ./

RUN chmod +x gradlew || true
# 2) 의존성만 미리 내려받아 캐시 생성 (소스 미복사)
#    --no-daemon: 컨테이너에서 데몬 없이
RUN ./gradlew --no-daemon dependencies || true


RUN ls -R
# 3) 소스 복사 후 빌드
COPY src ./src
RUN ./gradlew --no-daemon clean build -x test

# ---------- Run stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
# JAR 경로는 프로젝트에 따라 다를 수 있음: *-SNAPSHOT.jar 또는 *-plain.jar 등 확인
COPY --from=build /workspace/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.profiles.active=prod"]