 
# Project B: Collaborative Task Management System by Chill Guys

## Overview
The **Collaborative Task Management System** is designed to streamline team-based task assignments, project tracking, and user collaboration. It provides user authentication, role-based access control, and a structured workflow for managing tasks efficiently.

---

## Technologies Used
### Backend:
- **Language & Framework:** Java (Spring Boot)
- **Authentication:** JWT-based Authentication & Authorization
- **Data Persistence:** MySQL Database using JPA/Hibernate
- **Security:** Spring Security with Role-Based Access Control (RBAC)
- **Dependency Management:** Maven
- **Logging & Monitoring:** Spring Boot Actuator, Log4j

### Frontend:
- **Framework:** React.js (with functional components & hooks)
- **State Management:** Redux Toolkit
- **UI Library:** Material UI for styling
- **Routing:** React Router
- **API Communication:** Axios for RESTful API calls
## Core Features
### Authentication & User Management
- User Registration & Login (JWT-based)
- Role-Based Access Control (Admin, User)
- Secure password storage with BCrypt hashing

### Task Management
- Create, Read, Update, Delete (CRUD) tasks
- Assign tasks to users
- Track task progress and status updates
- Due date and deadline tracking
---

## Testing Strategy
### Backend Testing
- **Unit Tests:** JUnit & Mockito for service layer testing
- **Integration Tests:** Using Spring Boot Test framework
- **Authentication Testing:** MockMvc for testing secured endpoints
- **Database Testing:** H2 in-memory database for integration tests

### Frontend Testing
#### Test Architecture
- **Testing Framework:** Cypress  
- **Testing Type:** End-to-End (UI/Interaction Focus)  

#### Test Structure
1. **Unit Testing:**  
   - Using Jest & React Testing Library  
   - Covers UI components, reducers, utility functions

2. **End-to-End (E2E) Testing:**  
   - Using Cypress  
   - Simulates real user interactions like login, task creation, updating, and deletion.

#### Future Enhancements
- Add `data-testid` attributes to all interactive elements.
- Implement visual regression testing.
- Automate keyboard navigation tests.

### API Endpoints

#### Task Creation
- **Endpoint:** `POST /api/tasks/create`
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
- **Endpoint:** `DELETE /api/tasks/delete/{id}`
- **Description:** Deletes an existing task identified by its unique ID.
- **Response:** Returns a success message with an HTTP 200 status if the deletion is successful, or an HTTP 404 if the task is not found.

#### Other Endpoints
- **GET `/api/tasks/{id}`:** Retrieves a task by its ID.
- **POST `/api/tasks/update/{id}`:** Updates an existing task.

### Data Transfer

Data is transmitted between the client and server using Data Transfer Objects (DTOs). For example, the `TaskDTO` class encapsulates the task information required by the API, ensuring that only the necessary data is exposed while maintaining a separation from the internal domain model.

### Design Patterns Implemented

1. **Decorator Pattern**
Purpose: Dynamically adds responsibilities to TaskInterface objects without altering their core structure.

Implementation: The TaskDecorator class (located in com.TaskCollab.Decorator) acts as an abstract base decorator. Concrete decorators like LoggingTaskDecorator and ValidationTaskDecorator extend TaskDecorator to add specific behaviors (logging, validation, etc.) to TaskInterface implementations.

**Benefits:**
Allows for flexible and dynamic addition of functionality.
Promotes the Open/Closed principle (open for extension, closed for modification).
Avoids the creation of numerous subclasses for each combination of features.

2. **Data Transfer Object (DTO) Pattern**
   - **Purpose:** Separates internal data representations from the API contract.
   - **Implementation:** The `TaskDTO` class is used to transfer data between the Controller and Service layers, ensuring that the domain models remain encapsulated and only the necessary fields are exposed.
   - **Benefits:** Enhances security, simplifies serialization, and decouples API changes from internal model changes.

## Project Structure

- **Controllers:** Define REST endpoints (e.g., `TaskController`, `AuthController`).
- **Services:** Implement business logic (e.g., `TaskService`, `UserService`).
- **Repositories:** Handle database interactions using Spring Data JPA (e.g., `TaskRepository`, `UserRepository`).
- **Security:** Configure JWT authentication and authorization (e.g., `SecurityConfig`, `JwtUtils`, `JwtAuthFilter`).
- **DTOs:** Classes such as `TaskDTO`, `LoginRequest`, and `LoginResponse` for data transfer between layers.

