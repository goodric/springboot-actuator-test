# Spring Boot Actuator Test

This is a Spring Boot application with Actuator endpoints enabled.

## Requirements

- Java 8 or higher
- Maven 3.0.5

## Building the Application

```bash
mvn clean package
```

## Running the Application

```bash
java -jar target/spring-boot-test-1.0-SNAPSHOT.jar
```

## Available Actuator Endpoints

The application exposes all Actuator endpoints on port 8080. You can access them at:

- Actuator index: http://localhost:8080/actuator
- Health endpoint: http://localhost:8080/actuator/health
- Info endpoint: http://localhost:8080/actuator/info
- Metrics endpoint: http://localhost:8080/actuator/metrics
- Gateway endpoint: http://localhost:8080/actuator/gateway

## Configuration

All Actuator endpoints are enabled and exposed. The configuration can be found in `src/main/resources/application.properties`. 