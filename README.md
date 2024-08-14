# Task-Management
This is a simple Task Management RESTful API built with Java Spring Boot. The API allows users to manage tasks and associate them with specific users. The tasks and users are stored in a PostgreSQL database,
with all date and time information stored in UTC.

# Prerequisites
Java 17
Maven 3.9.6 or higher
PostgreSQL 16 or higher

# Setup and Running the Application
git clone https://github.com/SanaFirojManer/Task-Management.git
cd task-management

# Set Up PostgreSQL Database
CREATE DATABASE taskmanager;

Configure Application Properties
spring.application.name=Task Manager
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show-sql=true
spring.jpa.show-sql=true
spring.datasource.url=jdbc:postgresql://localhost:5432/taskManager
spring.datasource.username=postgres
spring.datasource.password=Sana@123
management.endpoints.web.exposure.include=mappings

# Build and Run the Application
mvn clean install
mvn spring-boot:run

# API Endpoints
Task Endpoints
Create a Task: POST /api/tasks
Get All Tasks: GET /api/tasks
Get Task by ID: GET /api/tasks/{id}
Update a Task: PUT /api/tasks/{id}
Delete a Task: DELETE /api/tasks/{id}
User Endpoints
Create a User: POST /api/users
Get All Users: GET /api/users
Get User by ID: GET /api/users/{id}
Update a User: PUT /api/users/{id}
Delete a User: DELETE /api/users/{id}

# Assumptions and Considerations
All date and time information is stored in UTC in the PostgreSQL database.
Task status is managed using an enum with the values PENDING, IN_PROGRESS, and COMPLETED.
User management includes first name, last name, timezone, and active status.
Tasks must be assigned to a user, and the timezone for tasks is optional. If not provided, it defaults to the user's timezone.

# Unit Test Coverage
Service package 100% covered.

