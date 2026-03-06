FROM maven:3.9.11-amazoncorretto-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine
WORKDIR /usr/app
COPY --from=build /app/target/Bank_REST-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]