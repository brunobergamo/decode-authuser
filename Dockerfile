FROM openjdk:11-jdk-slim
RUN mkdir -p /workspace
WORKDIR /workspace
COPY /target/*.jar app.jar
COPY /target/classes/application-docker.yaml application-docker.yaml
EXPOSE 8080
EXPOSE 443
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","app.jar"]