# Stage 1: Build — imagem completa com Maven + JDK 21
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copia apenas o pom.xml primeiro para cachear a camada de dependências.
# Essa camada só é reconstruída quando o pom.xml mudar — não quando o código mudar.
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# Stage 2: Runtime — imagem mínima com apenas o JRE Alpine (~90MB vs ~500MB do JDK)
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuário não-root dedicado: containers não devem rodar como root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# -Djava.security.egd acelera o startup do Tomcat em ambientes de container
# onde /dev/random pode bloquear por falta de entropia
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]