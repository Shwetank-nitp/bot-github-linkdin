FROM amazoncorretto:17-alpine
COPY target/userService-0.0.1-SNAPSHOT.jar /home/userService-0.0.1-SNAPSHOT.jar
CMD ["sh", "-c", "sleep 10s && java -jar /home/userService-0.0.1-SNAPSHOT.jar"]
