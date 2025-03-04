# Task-Collab-BackEnd

Task Collaboration Project for COSC 310
# Task Collab BackEnd

## Overview

Task Collab BackEnd is a Spring Boot application that provides RESTful APIs for managing tasks and user authentication. It leverages Spring Security with JWT for secure access and employs design patterns to ensure clean, maintainable code.

## Feature Planning for Task Creation and Deletion

This section outlines the planning for implementing task creation and deletion features, including API endpoints, data transfer details, and design patterns.

### API Endpoints

#### Task Creation
- **Endpoint:** `POST /api/tasks`
- **Description:** Creates a new task in the system.
- **Request Body:** Expects a JSON payload that follows the `TaskDTO` format:
    ```json
    {
      "taskTitle": "Design UI",
      "description": "Design the user interface for the new module",
      "assignedTo": 1,
      "status": "Pending",
      "deadline": "2025-03-31T23:59:59"
    }
    ```
- **Response:** Returns the created task data with an HTTP 201 (Created) status.

#### Task Deletion
- **Endpoint:** `DELETE /api/tasks/{id}`
- **Description:** Deletes an existing task identified by its unique ID.
- **Response:** Returns a success message with an HTTP 200 status if the deletion is successful, or an HTTP 404 if the task is not found.

#### Other Endpoints
- **GET `/api/tasks/{id}`:** Retrieves a task by its ID.
- **PUT `/api/tasks/{id}`:** Updates an existing task.

### Data Transfer

Data is transmitted between the client and server using Data Transfer Objects (DTOs). For example, the `TaskDTO` class encapsulates the task information required by the API, ensuring that only the necessary data is exposed while maintaining a separation from the internal domain model.

### Design Patterns Implemented

1. **Builder Pattern**
   - **Purpose:** Simplifies and modularizes the construction of complex `Task` objects.
   - **Implementation:** The `TaskBuilder` class (located in `com.TaskCollab.util`) provides a fluent API to set task properties (e.g., title, description, assigned user, status, deadline) and then build a complete `Task` object.
   - **Benefits:** Improves code readability, reduces the need for multiple constructors, and centralizes object creation logic.

2. **Data Transfer Object (DTO) Pattern**
   - **Purpose:** Separates internal data representations from the API contract.
   - **Implementation:** The `TaskDTO` class is used to transfer data between the Controller and Service layers, ensuring that the domain models remain encapsulated and only the necessary fields are exposed.
   - **Benefits:** Enhances security, simplifies serialization, and decouples API changes from internal model changes.

## Project Structure

- **Controllers:** Define REST endpoints (e.g., `TaskController`, `AuthController`).
- **Services:** Implement business logic (e.g., `TaskService`, `UserService`).
- **Repositories:** Handle database interactions using Spring Data JPA (e.g., `TaskRepository`, `UserRepository`).
- **Security:** Configure JWT authentication and authorization (e.g., `SecurityConfig`, `JwtUtils`, `JwtAuthFilter`).
- **Utilities:** Helper classes like `TaskBuilder` in `com.TaskCollab.util` for constructing domain objects.
- **DTOs:** Classes such as `TaskDTO`, `LoginRequest`, and `LoginResponse` for data transfer between layers.

## Getting Started

1. **Clone the repository:**
    ```bash
    git clone https://github.com/TaskCollab/Task-Collab-BackEnd.git
    cd Task-Collab-BackEnd
    ```

2. **Build and run the application:**
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

3. **Test the API Endpoints:**  
   Use tools like Postman or cURL to interact with the API endpoints. Swagger or similar API documentation tools may also be integrated.


## UML Class Diagram

+--------------------------------+
|             Task               |
+--------------------------------+
| - id: Long                     |
| - taskTitle: String            |
| - description: String          |
| - assignedTo: Long             |
| - status: String               |
| - deadline: LocalDateTime      |
+--------------------------------+

            ↑
            | builds
            |
+--------------------------------+
|          TaskBuilder           |
+--------------------------------+
| - task: Task                   |
+--------------------------------+
| + TaskBuilder()                |
| + withTitle(title: String): TaskBuilder        |
| + withDescription(desc: String): TaskBuilder   |
| + withAssignedTo(assignedTo: Long): TaskBuilder  |
| + withStatus(status: String): TaskBuilder        |
| + withDeadline(deadline: LocalDateTime): TaskBuilder |
| + build(): Task                |
+--------------------------------+
+--------------------------------+
|           TaskDTO              |
+--------------------------------+
| + id: Long                     |
| + taskTitle: String            |
| + description: String          |
| + assignedTo: Long             |
| + status: String               |
| + deadline: LocalDateTime      |
+--------------------------------+

