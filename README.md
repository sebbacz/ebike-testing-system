# Ebike Testing System

A Spring Boot application that manages the testing process for e-bikes using a PostgreSQL database. This project is set up with Gradle and includes Docker Compose for easy database provisioning. It also demonstrates how to use Spring Profiles to separate development and production environments.

---

## Table of Contents
1. [Project Description](#project-description)
2. [Team Members](#team-members)
3. [Prerequisites](#prerequisites)
4. [Project Structure](#project-structure)
5. [Build Instructions](#build-instructions)
6. [Run Instructions](#run-instructions)
    - [Running with Docker Compose (Development)](#running-with-docker-compose-development)
    - [Running in Production Mode](#running-in-production-mode)
7. [Spring Profiles](#spring-profiles)
8. [Database Configuration](#database-configuration)

---

## Project Description
The **Ebike Testing System** is designed to:
- Manage customer data and their e-bikes.
- Schedule and record test results performed by technicians on test benches.
- Generate reports on test outcomes.

This setup is ideal for a team-based environment, ensuring that each developer can quickly spin up the application with minimal configuration.

---

## Team Members
- **Elias Akawi** 
- **Mirislom Mirabdivakhobov** 
- **Pavel Nikolov** 
- **Sebastian Gondek** 
- **Sepp Verbuyst** 

---

## Prerequisites
- **Java 21**
- **Docker & Docker Compose**
- **Git**
- **Gradle**

---

## Project Structure
ebike-testing-system/
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com/yourteam/ebiketestingsystem
│  │  │     ├─ model       (Entities:Ebike, Customer, Technician)
│  │  │     ├─ repository  (Spring Data Repositories)
│  │  │     ├─ service     (Business logic: AuthService, TestService)
│  │  │     └─ controller  (REST or MVC controllers)
│  │  └─ resources
│  │     ├─ application.properties
│  │     ├─ application-development.properties
│  │     └─ application-production.properties
│  └─ test
│     └─ java
│        └─ com/yourteam/ebiketestingsystem  (Unit/Integration tests)
├─ build.gradle
├─ settings.gradle
├─ docker-compose.yml
├─ README.md
└─ .gitignore

**Why the Structure?**
- **`src/main/java`**: Contains all production source code organized under a package (e.g., `com.yourteam.ebiketestingsystem`) to avoid naming conflicts and improve clarity.
   - **`model/`**: Houses your JPA entities (e.g., Ebike, Customer, Technician) which represent the domain objects mapped to your database.
   - **`repository/`**: Contains interfaces extending Spring Data JPA repositories, isolating data access logic.
   - **`service/`**: Holds business logic that manipulates data and orchestrates application operations. This keeps your code modular.
   - **`controller/`** (optional): If your app exposes web endpoints, controllers manage HTTP requests.
- **`src/main/resources`**: Contains configuration files:
   - **`application.properties`**: Default configuration.
   - **Profile-specific properties**: `application-development.properties` and `application-production.properties` separate environment-specific settings.
- **`src/test/java`**: Contains test code for unit and integration testing, kept separate from production code.
- **Build Files (`build.gradle` and `settings.gradle`)**: Define build configurations, dependencies, and tasks.
- **`docker-compose.yml`**: Provides a consistent way to spin up a PostgreSQL container for development.
- **`README.md` & `.gitignore`**: Offer documentation and prevent unwanted files from being version-controlled.

- This structure follows established conventions for Spring Boot and Gradle projects, making it easier for anyone familiar with these tools to navigate and contribute to the project.
---

## Build Instructions

You can build the project using the **Gradle Wrapper** included in the repository. This ensures you use the exact Gradle version configured for the project.

- **On Linux/Mac**:
  ```bash
  ./gradlew clean build
- **On Windows**:
   ```bash
  gradlew.bat clean build

## Run Instructions

## Running with Docker Compose (Development)

  ```bash
  docker-compose up -d
  ```
- **Start the PostgreSQL container**:

  ```bash
  ./gradlew bootRun --args='--spring.profiles.active=development'
  ```
- **Verify the application**:

  - Check the logs to ensure the application connects to the dev_db.
  - The application is accessible at http://localhost:8080.

---

# E-Bike Testing System

This document provides instructions for running the application in production mode, explains the available Spring profiles, details the database configuration, and outlines optional features, known issues, and next steps.

---

## Running in Production Mode

1. **Update Production Settings**  
   Update `application-production.properties` with your production database settings.

2. **Run the Application**  
   You can start the application using one of the following commands:

   - Using Gradle:
     ```bash
     ./gradlew bootRun --args='--spring.profiles.active=production'
     ```
   - Using the JAR file:
     ```bash
     java -jar build/libs/ebike-testing-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
     ```

---

## Spring Profiles

- **Default Profile**:  
  Loads settings from `application.properties`.

- **Development Profile** (`application-development.properties`):
   - Uses Docker-based PostgreSQL (`jdbc:postgresql://localhost:5432/dev_db`).
   - Typically sets `spring.jpa.hibernate.ddl-auto=update` for convenience.

- **Production Profile** (`application-production.properties`):
   - Points to a production-grade database.
   - Typically sets `spring.jpa.hibernate.ddl-auto=none`.

**Activation:**  
Activate a profile at runtime with the parameter:
   ```bash
   --spring.profiles.active=<profile>
  ```
---
## Database Configuration

The provided `docker-compose.yml` sets up a PostgreSQL container with the following credentials:

- **Database**: `PostgresSQL`
- **Username**: `postgres`
- **Password**: `Student123`
- **Port**: `5432` on localhost

- Ensure these settings match the ones in your `application-development.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dev_db
spring.datasource.username=dev_user
spring.datasource.password=dev_password
spring.jpa.hibernate.ddl-auto=update
```

---

# Contact

For any questions or contributions, please reach out to [Sepp Verbuyst] at [sepp.verbuyst@student.kdg.be].
