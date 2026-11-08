# UserAuthService

A Spring Boot REST API for user registration and login, issuing JWTs and protecting authenticated endpoints.

## Requirements

- Java 17
- MySQL
- Maven (or the included Maven Wrapper)

## Configuration

The default configuration connects to a MySQL database named `userauthservice` on `localhost:3306` and runs on port `8081`.

Before starting the application:

1. Create the `userauthservice` database.
2. Configure the database credentials expected by `src/main/resources/application.properties`. The database username is read from the `DB_USERNAME` environment variable.
3. Set `JWT_SECRET_KEY` to a secure secret. Keep credentials and secrets out of source control.

The `prod` Spring profile disables SQL logging and validates the database schema instead of updating it.

## Build and test

On Windows:

```powershell
.\mvnw.cmd clean test
```

## Run

Start with the default profile:

```powershell
.\mvnw.cmd spring-boot:run
```

Start with the production profile:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=prod"
```

The API is available at `http://localhost:8081`.

## API

### Sign up

`POST /api/auth/signup`

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

The email must be valid, and the password must be 8–20 characters. A successful response includes the user ID, email, and role. New users receive the `USER` role.

### Log in

`POST /api/auth/login`

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

A successful response contains a JWT:

```json
{
  "token": "<jwt>"
}
```

### Validate a token

`GET /api/auth/validateToken`

Send the token in the `Authorization` header:

```text
Authorization: Bearer <jwt>
```

### Get the current user

`GET /user/me`

Requires a valid JWT in the `Authorization` header.

### Health check

`GET /api/auth/healthcheck`

Returns `Up and running` when the service is available.

### Hello

`GET /api/auth/hello`

Returns `Hello World`.

## Authentication

The service is stateless and uses bearer JWTs. Authentication endpoints under `/api/auth/**` are publicly accessible; other endpoints require a valid token.
