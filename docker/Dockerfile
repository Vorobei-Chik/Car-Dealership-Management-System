# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

COPY common/build.gradle common/build.gradle
COPY common/src/main/proto common/src/main/proto
COPY order-service/build.gradle order-service/build.gradle
COPY storage-service/build.gradle storage-service/build.gradle

ARG SERVICE_NAME=order-service
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x gradlew \
    && ./gradlew :${SERVICE_NAME}:dependencies --no-daemon

COPY common ./common
COPY order-service ./order-service
COPY storage-service ./storage-service

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew :${SERVICE_NAME}:bootJar -x test -x integrationTest --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

ARG SERVICE_NAME=order-service
COPY --from=build /workspace/${SERVICE_NAME}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
