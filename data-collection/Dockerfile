# Base image for Java
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR to the container
COPY build/libs/data-collection-0.0.1-SNAPSHOT.jar app.jar

# Set environment variable for Elasticsearch host
ENV ELASTICSEARCH_HOST=http://elasticsearch:9200

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
