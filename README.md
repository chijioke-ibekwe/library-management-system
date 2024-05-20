# Library Management System
RESTful APIs for a library management application built on Spring Boot 3

## Getting Started
### Prerequisites
To successfully run this project, you'll need the following installed on your machine:
- **JDK >= v17.** That is because this project was developed using Spring Boot 3, which requires Java 17
  as a minimum version.
- **Docker**

## 🎈 How to Run
To run this project:
1. Clone the repository using the following command:
   ```
   git clone https://github.com/chijioke-ibekwe/library-management-system.git
   ```

2. A docker compose file is attached to this project to help you easily set up your development databases. To do this,
   run the following command on your terminal or command line, from the root directory of the project
   ```
   docker compose up -d
   ```
   This command will spin-up the following containers running in a detached mode:
    - A postgres RDBMS running on `localhost:5431`
    - A pgAdmin UI running on `localhost:5050` for managing the database, and
    - A redis database running on `localhost:6379`

3. Use the `pgAdmin` interface, running on `localhost:5050` to interact with the `library-manager` database already created.

4. To run tests, run the following command from the root directory of the project:
   ```
   ./mvnw test
   ```
   
5. To start the application, run the following command:
   ```
   ./mvnw clean compile exec:java
   ```
   Liquibase will run the migration files within the `src/main/resources/db` directory, upon startup, to set up the 
   database schema. The application will be running on `http://localhost:8081`

6. The Swagger API documentation for this project will be running on `http://localhost:8081/swagger-ui/index.html`

7. All the endpoints are protected (using Spring Security) aside from the user sign up, user login, and swagger docs 
   endpoints. Hence, you should take the following initial steps:
   - Register a user using the sign up API `http://localhost:8081/swagger-ui/index.html#/user-controller/registerUser`
   - Login with your credentials using the login API `http://localhost:8081/swagger-ui/index.html#/authentication-controller/login`
   - Copy the access token returned from the step above
   - Add it as a bearer token in the Authorization header of all subsequent calls to other APIs
