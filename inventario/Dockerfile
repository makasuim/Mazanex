# Etapa de build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom y código del microservicio
COPY pom.xml .
COPY src ./src

# Compilamos el JAR
RUN mvn -q -DskipTests package

# Etapa de runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

# Puerto por defecto (solo para correr localmente con Docker)
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
