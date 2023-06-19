FROM openjdk:17-jdk-alpine
MAINTAINER Nastia
COPY build/libs/somnusAuth-0.0.1-SNAPSHOT.jar user-server-1.0.0.jar
VOLUME /home/nastia/javaProjects/ava/:/home/nastia/javaProjects/ava/
ENTRYPOINT ["java", "-jar", "/user-server-1.0.0.jar"]