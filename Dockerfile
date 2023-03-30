FROM gradle:7.6.0-jdk17 as build
WORKDIR /app
COPY / .
RUN chmod +x ./gradlew
RUN gradle bootJar

FROM openjdk:17-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /app/build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]