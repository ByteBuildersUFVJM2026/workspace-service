
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build


COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -Dmaven.test.skip=true -q


FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL org.opencontainers.image.source="https://github.com/ByteBuildersUFVJM2026/workspace-service"

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
