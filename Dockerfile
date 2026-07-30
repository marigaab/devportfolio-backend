# Etapa 1: Build (Compilação com Java 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY portifolio-api /app
RUN mvn clean package -DskipTests

# Etapa 2: Execução leve
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]