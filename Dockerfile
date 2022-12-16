# syntax=docker/dockerfile:1

FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:resolve
COPY src ./src
RUN ./mvnw package -DskipTests

FROM base as build
EXPOSE 8080
COPY --from=base /app/target/contact-manager-*.jar /contact-manager.jar
CMD ["java", "-jar", "/contact-manager.jar"]