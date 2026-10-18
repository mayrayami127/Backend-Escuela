FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -pl Alumno -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/Alumno/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
