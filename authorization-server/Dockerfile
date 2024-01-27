# syntax=docker/dockerfile:1
FROM eclipse-temurin:20-jdk-alpine as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:20-jdk-alpine as production
COPY --from=build /app/target/authorization-*.jar /auth.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/auth.jar"]
