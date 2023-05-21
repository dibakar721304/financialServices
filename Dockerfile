FROM openjdk:11-jre-slim

WORKDIR /app
COPY ./target/banking-services-0.0.1-SNAPSHOT.jar /app


CMD ["java", "-jar", "banking-services-0.0.1-SNAPSHOT.jar"]