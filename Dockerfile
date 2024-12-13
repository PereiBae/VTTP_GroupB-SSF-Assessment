FROM openjdk:23-jdk-oracle AS builder

ARG COMPILE_DIR=/compiledir

WORKDIR ${COMPILE_DIR}

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

## Build the application
RUN ./mvnw package -Dmaven.test.skip=true

## How to run the applicaiton
#ENV SERVER_PORT=8080
# for Railway
ENV PORT=8080
ENV NOTICEBOARD_DB_USERNAME=""
ENV NOTICEBOARD_DB_PASSWORD=""
ENV NOTICEBOARD_DB_HOST=localhost
ENV NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_DATABASE=0
ENV REST_API_LINK=""

EXPOSE ${PORT}

# App will run in second stage
# ENTRYPOINT SERVER_PORT=${PORT} java -jar target/Day18-0.0.1-SNAPSHOT.jar

## Day 18 - slide 13
## The second stage
FROM eclipse-temurin:23-jre-noble

ARG WORK_DIR=/app

WORKDIR ${WORK_DIR}

COPY --from=builder /compiledir/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV NOTICEBOARD_DB_USERNAME=""
ENV NOTICEBOARD_DB_PASSWORD=""
ENV NOTICEBOARD_DB_HOST=localhost
ENV NOTICEBOARD_DB_PORT=6379
ENV NOTICEBOARD_DB_DATABASE=0
ENV REST_API_LINK=""

EXPOSE ${PORT}

HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 CMD curl -s -f http://localhost:8080/status || exit 1

ENTRYPOINT java -jar app.jar