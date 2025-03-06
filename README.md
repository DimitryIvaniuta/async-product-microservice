# Async Microservice: Products, Users, and Orders Processing

This project demonstrates a professional, real-world Spring Boot microservices application built using Java 21 and Gradle. It implements asynchronous processing with `CompletableFuture.supplyAsync` and robust error handling using global exception handling. The application models the following business domains:

- **Products:** Items that can be purchased.
- **Users:** Customers who can buy products.
- **Orders (Purchases):** An intermediate entity representing a purchase made by a user for a product, capturing additional details such as purchase date and quantity.

Additionally, the project uses a PostgreSQL data source running in Docker.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
  - [Docker Compose for PostgreSQL](#docker-compose-for-postgresql)
  - [application.yml](#applicationyml)
- [Entities and Relationships](#entities-and-relationships)
- [Asynchronous Processing](#asynchronous-processing)
- [Global Exception Handling](#global-exception-handling)
- [Controllers](#controllers)
- [How to Run](#how-to-run)
- [Endpoints](#endpoints)
- [Technologies Used](#technologies-used)
- [License](#license)

---

## Overview

This project demonstrates:

- **Asynchronous Processing:**  
  Using `CompletableFuture.supplyAsync` in Spring Boot services to fetch data concurrently from various sources.
- **Global Exception Handling:**  
  Utilizing `@ControllerAdvice` to handle exceptions uniformly and return a structured error response.
- **Robust Data Modeling:**  
  Modeling Users, Products, and Orders using JPA. Instead of a direct many-to-many mapping, an intermediate `Purchase` entity is used to capture extra details (e.g., purchase date, quantity) and to improve performance and maintainability.
- **Dockerized PostgreSQL:**  
  Running PostgreSQL in a Docker container via Docker Compose.

---

## Features

- **Async Microservices:**  
  Product and User services fetch data asynchronously and combine results where needed.
- **Intermediate Purchase Entity:**  
  Models the relationship between Users and Products, enabling many users to buy the same product with additional details.
- **Global Exception Handling:**  
  Custom exceptions such as `NotFoundException` and a global handler manage errors across the application.
- **Docker Integration:**  
  Uses Docker Compose to run a PostgreSQL database, ensuring a consistent and isolated environment.
- **Modern Java and Spring Boot:**  
  Built with Java 21, Spring Boot 3.x, and Gradle.

---

## Project Structure

```
project-root/
├── build.gradle
├── docker-compose.yml
└── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── microservices
        │               ├── MicroservicesApplication.java
        │               ├── config
        │               │   └── AsyncConfig.java
        │               ├── controller
        │               │   ├── ProductController.java
        │               │   └── UserController.java
        │               ├── exception
        │               │   └── NotFoundException.java
        │               ├── advice
        │               │   ├── GlobalExceptionHandler.java
        │               │   └── CustomErrorResponse.java
        │               ├── model
        │               │   ├── Product.java
        │               │   ├── User.java
        │               │   └── Purchase.java
        │               └── service
        │                   ├── ProductService.java
        │                   └── UserService.java
        └── resources
            └── application.yml
```

---

## Prerequisites

- Java 21
- Gradle
- Docker & Docker Compose

---

## Configuration

### Docker Compose for PostgreSQL

Create a file named `docker-compose.yml` in your project root:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    container_name: postgres-db
    environment:
      POSTGRES_DB: microservicesdb
      POSTGRES_USER: microservices_user
      POSTGRES_PASSWORD: secret_password
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
volumes:
  pgdata:
```

### application.yml

Place this file in `src/main/resources/application.yml`:

```yaml
server:
  port: 8080

spring:
  application:
    name: microservices-application
  datasource:
    url: jdbc:postgresql://localhost:5432/microservicesdb
    username: microservices_user
    password: secret_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
  main:
    allow-bean-definition-overriding: true

logging:
  level:
    root: INFO
    com.example.microservices: DEBUG

management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```

---

## How to Run

1. **Start PostgreSQL:**

   Run Docker Compose from the project root:
   ```bash
   docker-compose up -d
   ```

2. **Build the Application:**

   Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

3. **Run the Application:**

   Start the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```
   or run the generated JAR:
   ```bash
   java -jar build/libs/your-application.jar
   ```

4. **Test the Endpoints:**

  - **Product Service:**  
    `GET http://localhost:8080/products/{id}`
  - **User Service:**  
    `GET http://localhost:8080/users/{id}`

---

## Technologies Used

- Java 21
- Spring Boot 3.x
- Gradle
- PostgreSQL (Dockerized via Docker Compose)
- Spring Data JPA / Hibernate
- CompletableFuture for asynchronous processing
- Global exception handling using @ControllerAdvice

---

## Contributors
- **Dzmitry Ivaniuta** - [GitHub Profile](https://github.com/DimitryIvaniuta)

---

## License
This project is provided for educational purposes. You may use and modify the code as needed.

