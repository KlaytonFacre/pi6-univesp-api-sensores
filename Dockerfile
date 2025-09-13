# syntax=docker/dockerfile:1.7

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .

ARG BUILD_VERSION=dev
ENV BUILD_VERSION=${BUILD_VERSION}

# cache do ~/.m2 acelera builds repetidos
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

ARG BUILD_VERSION=dev
ENV BUILD_VERSION=${BUILD_VERSION}
ENV APP_BUILD_VERSION=${BUILD_VERSION}

COPY --from=build /app/target/*.jar app.jar
COPY docker/entrypoint.sh /entrypoint.sh

# se editar no Windows, previne \r\n quebrar o script:
RUN sed -i 's/\r$//' /entrypoint.sh && chmod +x /entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
CMD ["java","-jar","app.jar"]
