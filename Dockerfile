FROM openjdk:latest
COPY /target/auth-server-1.0.1-SNAPSHOT.jar /home/auth-server-1.0.1-SNAPSHOT.jar
COPY qpon-google-secret.json /usr/local/bin/qpon-google-secret.json
WORKDIR /home
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "auth-server-1.0.1-SNAPSHOT.jar"]
