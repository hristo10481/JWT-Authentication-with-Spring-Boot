# User Management System

## Description
This is a Spring Boot project that utilizes Maven and provides the following functionalities:
1. Register (with JWT authentication and authorization)
2. Login (using JWT authentication and authorization)
3. Update Profile
4. View Profile
5. Upload Avatar/Profile Picture

## Prerequisites
To start and work with this project, you need to have the following installed:
- Java JDK (version 11 or later)
- Maven
- MySQL

## Installation

1. Clone the repository or download and unzip the ZIP archive.
2. Open a terminal and navigate to the project directory:

    ```bash
    cd path/to/user-management-system
    ```

3. Install all necessary dependencies using Maven:

    ```bash
    mvn clean install
    ```

## Configuration
The project uses MySQL for database management. You need to create a database and configure your `application.properties` file with the following parameters:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret_key
```
## Endpoints
# Register
Endpoint: /api/users/register
Method: POST
Description: Registers a new user with temporary JW token.
# Login
Endpoint: /api/users/login
Method: POST
Description: Logs in a user and returns a JWT token.
# Update Profile
Endpoint: /api/users/:userId
Method: PUT
Description: Updates the user's profile information.
# View Profile
Endpoint: /api/users/:userId
Method: GET
Description: Retrieves the user's profile information.
# Upload Avatar/Profile Picture
Endpoint: /api/users/:userId/avatar
Method: POST
Description: Uploads an avatare for the user.
