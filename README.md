# TaskTrack
TaskTrack is a Spring Boot RESTful application to manage tasks efficiently.  
Features include user registration, login, task CRUD, search, pagination, and tracking of active tasks.

## Tech Stack
- Spring Boot
- MySQL / H2
- REST APIs
- Maven

## How to Run
1. Clone the repository
2. Configure `application.properties`
3. Run the application using `mvn spring-boot:run`

## API Endpoints
- POST /api/users/register
- POST /api/users/login
- POST /api/tasks
- GET /api/tasks
- PUT /api/tasks/{id}
- DELETE /api/tasks/{id}

