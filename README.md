 
# Project B: Collaborative Task Management System by Chill Guys

## Project Status – Milestone 3

This project is a collaborative task management system developed using Java Spring Boot (backend) and React.js (frontend). It enables users and administrators to manage tasks, assign roles, and interact through a secure platform.

---

## Progress on Functional Requirements

| Requirement | Status | Notes |
|-------------|--------|-------|
| **CRUD Operations (Create, Read, Update, Delete) on Tasks** |  Completed | Fully implemented using `TaskService`, `TaskController`, and DTOs. |
| **Secure Admin Login** |  Completed | JWT-based authentication system using Spring Security. |
| **Role-Based Access Control** | Partially Completed | Roles and permissions are implemented in backend; admin role management UI is pending. |
| **Task Locking** | Not Implemented | Lock mechanism for tasks is not yet added; planned for next milestone. |
| **Task Search Functionality** |  Completed | Basic task retrieval implemented |
| **Notification System** | Completed (Basic) | Backend logic for sending notifications on role/task updates is in place; frontend display to be integrated. |
| **Unit Testing** | Completed | Testing completed with JUnit and Mockito. |

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

## Testing and Coverage

This project includes comprehensive unit and integration tests to ensure code quality and reliability. Test results, including code coverage metrics, are generated using JaCoCo. 

You can find the detailed JaCoCo report in the `testing/jacoco/index.html` file within the repository. Open this file in your web browser to view the coverage results.

## Known Bugs and Issues

* Misuse of wrapper Boolean and primitive type boolean

    * Description of the bug:
        * Incorrect usage of `Boolean` (wrapper class) and `boolean` (primitive type) in Java. This leads to unexpected behavior when handling null values in role filtering. The `Boolean` wrapper can represent null, while `boolean` converts null to false. This forces users to populate all filter fields, even when default values are not intended.
    * Steps to reproduce the bug :
        * 1. Attempt to filter roles using the API with some filter parameters set to null.
        * 2. Observe that the filter returns incorrect results due to null values being treated as false, instead of null.
        * 3. Observe that the api request requires all fields to be populated.
    * Affected components or features:
        * Role filtering API.
        * Data handling in role search functionalities.
    * Severity level: Moderate.

* "Manage users" button in ViewTasks.tsx is non-functional.

    * Description of the bug:
        * The "Manage users" button located on the ViewTasks.tsx page does not perform any action when clicked. It is intended to navigate the user to the user management section.
    * Steps to reproduce the bug:
        * 1. Navigate to the ViewTasks.tsx page.
        * 2. Locate the "Manage users" button.
        * 3. Click the "Manage users" button.
        * 4. Observe that no action is performed.
    * Affected components or features:
        * ViewTasks.tsx page.
        * User management navigation.
    * Severity level: Low.
    * Solution:
        * Import the `Link` component from `react-router-dom`.
        * Use `component={Link}` as a prop in the MUI Button, specifying the desired route for user management.

* Inconsistent UI Padding on Task Details Page.

    * Description of the bug:
        * The task details page exhibits inconsistent padding and spacing between elements, leading to a visually cluttered and unprofessional appearance. Specifically, the padding around the task description and assigned user sections varies significantly, making the page difficult to read.
    * Steps to reproduce the bug:
        * 1. Navigate to the task details page for any task.
        * 2. Observe the spacing and padding around the task description, assigned users, and other elements.
        * 3. Note the inconsistencies in padding values.
    * Affected components or features:
        * Task details page (ViewTaskDetails.tsx or similar).
        * CSS styling.
    * Severity level: Low.

* JWT Token Stored in Local Storage.

    * Description of the bug:
        * The application stores the JSON Web Token (JWT) in local storage. This creates a security vulnerability, as local storage is accessible by JavaScript and susceptible to cross-site scripting (XSS) attacks. If an attacker injects malicious JavaScript, they can steal the JWT and gain unauthorized access to the user's account.
    * Steps to reproduce the bug:
        * 1. Log in to the application.
        * 2. Open the browser's developer tools.
        * 3. Navigate to the "Application" or "Storage" tab.
        * 4. Observe the JWT stored in local storage.
    * Affected components or features:
        * Authentication system.
        * Token storage mechanism.
    * Severity level: Critical.
