# Docker Configuration for RememberMe Application with Spring Boot Buildpacks

This document provides instructions on how to build and run the RememberMe application using Spring Boot Buildpacks and Docker.

## Prerequisites

- Docker installed on your machine
- Docker Compose installed on your machine
- Maven installed on your machine (for building the image)

## Building and Running the Application

### Option 1: Using Spring Boot Buildpacks and Docker Compose (Recommended)

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Build the Docker image using Spring Boot Buildpacks:
   ```
   mvn spring-boot:build-image
   ```

   This command will:
   - Use Spring Boot's integrated Buildpacks support
   - Create an optimized Docker image named 'rememberme-backend'
   - Handle all dependencies and configurations automatically

3. Start the containers using Docker Compose:
   ```
   docker-compose up -d
   ```

   This command will:
   - Use the pre-built image created by Buildpacks
   - Start the PostgreSQL database
   - Start the backend application
   - Connect them together

4. The application will be available at:
   - API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

5. To stop the containers:
   ```
   docker-compose down
   ```

### Option 2: Building and Running Manually

1. Navigate to the backend directory:
   ```
   cd backend
   ```

2. Build the Docker image using Spring Boot Buildpacks:
   ```
   mvn spring-boot:build-image
   ```

3. Create a network for the containers:
   ```
   docker network create rememberme-network
   ```

4. Start the PostgreSQL container:
   ```
   docker run -d --name rememberme-postgres \
     --network rememberme-network \
     -p 5432:5432 \
     -e POSTGRES_DB=rememberme \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     postgres:17.5
   ```

5. Start the backend container:
   ```
   docker run -d --name rememberme-backend \
     --network rememberme-network \
     -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://rememberme-postgres:5432/rememberme \
     -e SPRING_DATASOURCE_USERNAME=postgres \
     -e SPRING_DATASOURCE_PASSWORD=postgres \
     rememberme-backend:latest
   ```

6. The application will be available at:
   - API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

## Troubleshooting

- If you encounter issues with the database connection, ensure the PostgreSQL container is running:
  ```
  docker ps
  ```

- To view logs from the backend container:
  ```
  docker logs rememberme-backend
  ```

- To view logs from the PostgreSQL container:
  ```
  docker logs rememberme-postgres
  ```
