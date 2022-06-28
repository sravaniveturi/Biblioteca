FROM openjdk:8-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} biblioteca.jar
ENTRYPOINT ["java","-jar","/biblioteca.jar"]