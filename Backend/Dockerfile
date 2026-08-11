FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/membernet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
USER 1001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
