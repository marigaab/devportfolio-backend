# Etapa 1: Build (Compilação com Java 25)
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY portifolio-api /app
RUN mvn clean package -DskipTests

# Etapa 2: Execução leve (JRE 25)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]