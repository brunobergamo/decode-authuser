FROM openjdk:11-jdk-oracle

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV SPRING_CONFIG_URL=fillout

EXPOSE 8082
CMD ["java", "-jar", "/app.jar"]