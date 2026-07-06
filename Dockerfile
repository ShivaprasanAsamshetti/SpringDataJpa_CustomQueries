FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY   target/SBpractice-0.0.1-SNAPSHOT.jar  app.jar
MAINTAINER "shiva"
EXPOSE  8080
ENTRYPOINT ["java","-jar","app.jar"]

