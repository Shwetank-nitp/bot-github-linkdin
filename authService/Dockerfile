FROM amazoncorretto:17-alpine
COPY target/authService-0.0.1-SNAPSHOT.jar /home/authService-0.0.1-SNAPSHOT.jar
CMD ["sh", "-c", "sleep 10s && java -jar /home/authService-0.0.1-SNAPSHOT.jar"]