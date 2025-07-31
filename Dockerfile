# Dockerfile for Java Spring Boot service
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/spring-boot-multi-api-0.0.1-SNAPSHOT.jar app.jar
COPY opentelemetry-javaagent.jar opentelemetry-javaagent.jar
COPY pyroscope.jar pyroscope.jar
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-Dotel.exporter.otlp.endpoint=http://otel-collector:4318", "-Dotel.exporter.otlp.protocol=http/protobuf", "-javaagent:/app/pyroscope.jar", "-Dpyroscope.server.address=http://pyroscope:4040", "-Dpyroscope.application.name=java-service-1", "-jar", "/app/app.jar"]