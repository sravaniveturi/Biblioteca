FROM openjdk:8-jdk-alpine
WORKDIR /home
COPY ./build/libs/biblioteca-0.0.1-SNAPSHOT.jar biblioteca.jar
ENTRYPOINT ["java","-jar","biblioteca.jar"]