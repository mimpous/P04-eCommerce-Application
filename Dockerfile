FROM 32bit/ubuntu:16.04
FROM openjdk:latest
COPY ./target/auth-course-0.0.1-SNAPSHOT.jar auth-course-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "auth-course-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080