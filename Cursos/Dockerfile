FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN --mount=type=cache,target=/root/.m2,sharing=locked mvn -q -pl Cursos -am -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/Cursos/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
