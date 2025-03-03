FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/Encurtador-de-URLs-0.0.4-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]