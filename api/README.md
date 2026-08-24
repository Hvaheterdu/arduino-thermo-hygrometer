# Arduino Thermo Hygrometer API

A Java RESTful API for an Arduino Thermo Hygrometer IoT device. The API stores and exposes temperature, air-humidity,
and battery measurements collected from the device.

## Overview

The API is built with:

- Java 25
- Spring Boot 4.1
- Spring MVC
- Spring Security
- Spring JDBC
- PostgreSQL
- Flyway database migrations
- Springdoc OpenAPI / Swagger UI
- Bucket4j for API rate limiting
- Testcontainers for integration tests
- Maven Wrapper

The API follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Cross-cutting concerns such as authentication, rate limiting, CORS, and error handling are handled separately through
Spring configuration and servlet filters.

## Requirements

Before running the API locally, install:

- Java 25
- Maven 3.9.11 or use the included Maven Wrapper
- Podman or Docker
- PostgreSQL 18.x, if you do not use the provided container setup

The project declares Java 25 and Maven 3.9.11 as required versions.

## Project structure

```text
api/
├── src/
│   ├── main/
│   │   ├── java/api/arduinothermohygrometer/
│   │   │   ├── configuration/     # Spring configuration
│   │   │   ├── controller/        # REST API controllers
│   │   │   ├── exception/         # Application exceptions and handlers
│   │   │   ├── filter/            # API-key authentication and rate limiting
│   │   │   ├── mapper/             # Domain/DTO mapping
│   │   │   ├── model/              # Persistence/domain models
│   │   │   ├── properties/         # Typed application properties
│   │   │   ├── provider/           # Authentication providers
│   │   │   ├── repository/         # Repository contracts and implementations
│   │   │   ├── service/             # Business logic contracts and implementations
│   │   │   └── util/                # Shared utilities
│   │   └── resources/
│   │       ├── db/migration/       # Flyway migrations
│   │       ├── openapi/             # OpenAPI contract
│   │       └── application*.yaml   # Environment configuration
│   └── test/
│       ├── java/                    # Unit and integration tests
│       └── resources/sql/           # Test database fixtures
├── mvnw
├── mvnw.cmd
├── pom.xml
└── sonar-project.properties
```

## Local database

The local and development Spring profiles use PostgreSQL on `localhost:5432` with the following database configuration:

```text
Database: arduinothermohygrometer-local
Username: postgres-local
Password: postgres-local
Host: localhost
Port: 5432
```

### Podman

Start PostgreSQL with:

```bash
podman run --name postgres-local \
  -e POSTGRES_USER=postgres-local \
  -e POSTGRES_PASSWORD=postgres-local \
  -e POSTGRES_DB=arduinothermohygrometer-local \
  -p 5432:5432 \
  -d postgres:18.3
```

The same command works with Docker by replacing `podman` with `docker`.

Flyway runs automatically when the application starts and applies migrations from:

```text
src/main/resources/db/migration
```

## Configuration

The base configuration is in `src/main/resources/application.yaml`.

Environment-specific configuration is provided by:

- `application-local.yaml`
- `application-dev.yaml`
- `application-test.yaml`
- `application-prod.yaml`

The API listens on port `5000` by default.

### API key

API endpoints under `/api/**` require API-key authentication.

The API key is read from the `API_KEY` environment variable in the default configuration and must be sent using the
`X-API-KEY` header.

Example:

```bash
export API_KEY="your-api-key"
```

Then include it in requests:

```http
X-API-KEY: your-api-key
```

The API is stateless and uses the API key to authenticate requests. Authenticated API requests require the `API_ADMIN`
role.

> Do not commit real API keys or other secrets to the repository.

## Running the API

### Local profile

From the `api` directory:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

On Windows:

```powershell
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

The API will be available at:

```text
http://localhost:5000
```

The local profile enables OpenAPI documentation and Swagger UI.

## Building

Create a production JAR with:

```bash
./mvnw clean package
```

The generated application JAR is placed in:

```text
target/
```

To skip tests when packaging:

```bash
./mvnw clean package -DskipTests
```

## Testing

Run unit tests:

```bash
./mvnw test
```

Run the complete verification lifecycle, including integration tests:

```bash
./mvnw verify
```

Integration tests use Testcontainers and therefore require a container runtime such as Podman or Docker.

The test profile uses an API key of `api-secret-key` and a local PostgreSQL configuration.

## Formatting and quality checks

The project uses Spotless with Google Java Format. Formatting is checked during the Maven `validate` phase.

Run the Maven lifecycle normally to execute the configured checks:

```bash
./mvnw verify
```

SonarQube configuration is provided through `sonar-project.properties`, and the Sonar Maven plugin is configured in the
Maven build.

## OpenAPI

The API contract is maintained at:

```text
src/main/resources/openapi/arduino-thermo-hygrometer-api.yaml
```

The specification uses OpenAPI `3.1.2`.

The Maven build also generates Spring API interfaces and DTOs from this specification during the `generate-sources`
phase. Generated sources are written under:

```text
target/generated-sources/openapi
```

The application controllers implement the generated API interfaces. This keeps the API contract and implementation
aligned.

## Swagger UI

Swagger UI is enabled for the local, development, and test profiles.

After starting the application locally, open:

```text
http://localhost:5000/swagger-ui.html
```

The generated OpenAPI document is available at:

```text
http://localhost:5000/v3/api-docs
```

Swagger UI and OpenAPI endpoints do not require an API key.

Swagger UI is disabled in the production profile.

## API endpoints

All application endpoints are under `/api/v1` and require the `X-API-KEY` header.

### Battery

| Method   | Endpoint            | Description                                        |
|----------|---------------------|----------------------------------------------------|
| `GET`    | `/api/v1/batteries` | Retrieve battery measurements by date or timestamp |
| `POST`   | `/api/v1/batteries` | Register a battery measurement                     |
| `DELETE` | `/api/v1/batteries` | Delete battery measurements by date or timestamp   |

Request/query parameters:

- `registeredAt` — required ISO-8601 date-time value.
- `dateOnly` — required boolean. When `true`, the date portion of `registeredAt` is used; when `false`, the timestamp is
  matched.

Example:

```bash
curl -H "X-API-KEY: your-api-key" \
  "http://localhost:5000/api/v1/batteries?registeredAt=2026-08-23T20:00:00&dateOnly=false"
```

Example request body:

```json
{
  "registeredAt": "2026-08-23T20:00:00Z",
  "batteryStatus": 75
}
```

`batteryStatus` must be between `0` and `100`.

### Humidity

| Method   | Endpoint             | Description                                         |
|----------|----------------------|-----------------------------------------------------|
| `GET`    | `/api/v1/humidities` | Retrieve humidity measurements by date or timestamp |
| `POST`   | `/api/v1/humidities` | Register a humidity measurement                     |
| `DELETE` | `/api/v1/humidities` | Delete humidity measurements by date or timestamp   |

Example request body:

```json
{
  "registeredAt": "2026-08-23T20:00:00Z",
  "airHumidity": 55.5
}
```

`airHumidity` must be between `20.00` and `90.00`.

### Temperature

| Method   | Endpoint               | Description                                            |
|----------|------------------------|--------------------------------------------------------|
| `GET`    | `/api/v1/temperatures` | Retrieve temperature measurements by date or timestamp |
| `POST`   | `/api/v1/temperatures` | Register a temperature measurement                     |
| `DELETE` | `/api/v1/temperatures` | Delete temperature measurements by date or timestamp   |

Example request body:

```json
{
  "registeredAt": "2026-08-23T20:00:00Z",
  "temp": 21.5
}
```

`temp` must be between `-55.00` and `125.00`.

## HTTP responses

Successful operations use the following status codes:

| Status           | Meaning                                  |
|------------------|------------------------------------------|
| `200 OK`         | A measurement was successfully retrieved |
| `201 Created`    | A measurement was successfully created   |
| `204 No Content` | Measurements were successfully deleted   |

Errors are returned using `application/problem+json` and follow RFC 9457-style problem details.

Common error responses include:

| Status                      | Meaning                                               |
|-----------------------------|-------------------------------------------------------|
| `400 Bad Request`           | Request validation failed                             |
| `401 Unauthorized`          | API key is missing                                    |
| `403 Forbidden`             | API key is invalid or does not have the required role |
| `404 Not Found`             | Requested resource was not found                      |
| `429 Too Many Requests`     | API rate limit was exceeded                           |
| `500 Internal Server Error` | Unexpected server-side error                          |

A problem response contains fields such as:

```json
{
  "type": "https://api.arduinothermohygrometer/errors/validation-error",
  "title": "Entity validation error.",
  "detail": "One or more fields are invalid.",
  "status": 400,
  "instance": "/api/v1/temperatures",
  "traceId": "00-example-example-00",
  "timestamp": "2026-08-23T20:00:00Z",
  "errors": []
}
```

For validation failures, `errors` contains information about the invalid parameter and its JSON pointer.

## Rate limiting

Requests to `/api/**` are rate limited using Bucket4j.

The current limit is:

```text
100 requests per 10 minutes
```

The API returns these headers for API requests:

- `X-RateLimit-Limit`
- `X-RateLimit-Remaining`
- `X-RateLimit-Reset`

When the limit is exceeded, the API responds with `429 Too Many Requests` and a `Retry-After` header.

Rate limiting is keyed by the API key. If no API key is supplied, the remote address is used as the rate-limit key
before authentication rejects the request.

## CORS

The default configuration permits requests from:

```text
http://localhost:3000
```

Allowed methods are:

```text
GET
POST
DELETE
```

Allowed request headers include:

```text
Accept
Content-Type
X-API-KEY
```

Update the environment-specific configuration if the frontend is served from another origin.

## Health and actuator endpoints

The following health endpoints are publicly accessible:

```text
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

The application exposes the following actuator endpoints according to its management configuration:

- `health`
- `info`
- `metrics`

Other actuator endpoints require the `ACTUATOR` role.

## Database migrations

Flyway manages database schema migrations.

Migrations are stored in:

```text
src/main/resources/db/migration
```

The initial schema is created by:

```text
V1__initial_migration.sql
```

Do not modify an already-applied migration in a shared environment. Add a new versioned migration for schema changes.

## Development workflow

A typical local workflow is:

```bash
# 1. Start PostgreSQL
podman start postgres-local

# 2. Set the API key
export API_KEY="your-api-key"

# 3. Start the API
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# 4. Run tests when making changes
./mvnw verify
```

For a clean database container:

```bash
podman rm -f postgres-local

podman run --name postgres-local \
  -e POSTGRES_USER=postgres-local \
  -e POSTGRES_PASSWORD=postgres-local \
  -e POSTGRES_DB=arduinothermohygrometer-local \
  -p 5432:5432 \
  -d postgres:18.3
```

## Related project directories

```text
arduino-thermo-hygrometer/
├── api/   # Java/Spring REST API
└── app/   # Frontend application
```

The frontend consumes the API contract from the OpenAPI specification rather than depending on implementation details of
the Java controllers.

## License

This project is licensed under the MIT License. See the repository `LICENSE` file for details.
