FROM store/oracle/jdk:11
RUN mkdir -p /src/api
WORKDIR /src/api
COPY build/libs/*.jar api.jar
EXPOSE 8080
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","api.jar"]
