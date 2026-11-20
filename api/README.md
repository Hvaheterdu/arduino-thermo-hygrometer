# Arduino Thermo Hygrometer API

Store and expose temperature, air-humidity, and battery readings collected from an Arduino device.

## Overview

The API uses Java 25, Spring Boot 4.1, Spring MVC, Spring Security, Spring JDBC, PostgreSQL,
and Flyway. The OpenAPI contract generates Spring interfaces and DTOs. Controllers delegate
to services and repositories; configuration and filters handle authentication, CORS, rate
limiting, and problem responses.

The [frontend](../app/README.md) consumes this API and also provides a standalone mock mode.
OpenAM is not required.

## Requirements

- Java 25; Maven Enforcer requires a version in `[25, 26)`.
- The included Maven 3.9.16 Wrapper, or a compatible installed Maven version satisfying
  [pom.xml](pom.xml).
- PostgreSQL 18.x for a live database.
- Docker or Podman with a Docker-compatible endpoint for Testcontainers integration tests.

Commands below run from `api/` unless stated otherwise. On Windows, use `mvnw.cmd` instead
of `./mvnw`.

## Getting started

### Local database

Start the database used by the local profile:

```sh
podman run --name postgres-local \
  -e POSTGRES_USER=postgres-local \
  -e POSTGRES_PASSWORD=postgres-local \
  -e POSTGRES_DB=arduinothermohygrometer-local \
  -p 127.0.0.1:5432:5432 \
  -d postgres:18.3
```

Replace `podman` with `docker` if preferred. These credentials are local-development
examples, not production settings. This container command does not configure a persistent
volume; do not remove the container if you need to retain its database.

For later sessions, start the existing container with `podman start postgres-local`.
Flyway applies migrations automatically when the API starts.

### JWT credentials

The API requires the following environment variables, including in the local profile:

```sh
export JWT_SECRET_BASE64="$(openssl rand -base64 64 | tr -d '\n')"
export JWT_BEARER_FORMAT="JWT"
export JWT_USERNAME="api-client"
export JWT_PASSWORD_HASH='{bcrypt}<bcrypt-password-hash>'
export JWT_ISSUER="arduino-thermo-hygrometer-api"
export JWT_AUDIENCE="arduino-thermo-hygrometer-api"
export JWT_ACCESS_TOKEN_TTL="PT30M"
```

Replace the password-hash placeholder with a BCrypt hash of your chosen password, prefixed
with `{bcrypt}` for Spring's delegating password encoder. The Base64 signing secret must
decode to at least 64 bytes. Generating a new secret invalidates previously issued tokens.
Do not commit signing secrets, real credentials, or access tokens.

### Start the API

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The API listens at <http://localhost:5000>. Local documentation is available at
<http://localhost:5000/swagger-ui.html> and <http://localhost:5000/v3/api-docs>.
Configure the [frontend's live mode](../app/README.md#live-api) to sign in with the same
username and the plaintext password corresponding to the configured hash.

## Configuration

### Spring profiles

The base settings are in [application.yaml](src/main/resources/application.yaml).

| Profile   | Configuration                                                           | API documentation |
| --------- | ----------------------------------------------------------------------- | ----------------- |
| `local`   | [application-local.yaml](src/main/resources/application-local.yaml)     | Enabled           |
| `dev`     | [application-dev.yaml](src/main/resources/application-dev.yaml)         | Enabled           |
| `test`    | [application-test.yaml](src/main/resources/application-test.yaml)       | Enabled           |
| `staging` | [application-staging.yaml](src/main/resources/application-staging.yaml) | Disabled          |
| `prod`    | [application-prod.yaml](src/main/resources/application-prod.yaml)       | Disabled          |

The checked-in local, development, staging, and production datasource settings all point
to the sample local database. Override `SPRING_DATASOURCE_URL`,
`SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` for deployed environments.
The staging/production profile names do not make those sample credentials deployment-ready.

### Authentication and authorization

`POST /auth/token` exchanges configured credentials for a short-lived JWT:

```sh
curl --request POST \
  --header "Content-Type: application/json" \
  --data '{"username":"api-client","password":"<your-password>"}' \
  http://localhost:5000/auth/token
```

Send the returned `accessToken` as a bearer token on `/api/**` requests. The API is stateless:
tokens are not stored server-side. HS512 signatures, issuer, audience, and token timestamps
are validated by Spring Security's resource server support.

Application endpoints require the `API_ADMIN` role. Non-public actuator endpoints require
`ACTUATOR`. Both roles are assigned to the configured client by the base configuration.
Review those privileges before deploying.

### CORS

The default allowed origin is `http://localhost:3000`. Allowed methods are `GET`, `POST`,
and `DELETE`; allowed request headers are `Accept`, `Content-Type`, and `Authorization`.
Update `cors.allowed-origins` when serving the frontend from another origin. Browser cookies
are not used for API authentication.

### Rate limiting

Default Bucket4j limits are:

| Requests                           | Bucket key  | Limit              |
| ---------------------------------- | ----------- | ------------------ |
| Authenticated `/api/**` requests   | JWT subject | 100 per 10 minutes |
| Unauthenticated `/api/**` requests | Client IP   | 100 per 10 minutes |
| `/auth/token` requests             | Client IP   | 5 per 10 minutes   |

Rate-limited responses include `X-RateLimit-Limit`, `X-RateLimit-Remaining`, and
`X-RateLimit-Reset` as implemented by the filters. Exhausted buckets return HTTP 429 with
`Retry-After`. Settings are under `rate-limit` in the base configuration.

## Project structure

```text
api/
  src/
    main/
      java/api/arduinothermohygrometer/
        configuration/       Spring configuration and JWT/security setup
        controller/          Implementations of generated API interfaces
        exception/           Exceptions and global problem handling
        filter/              Authentication and API rate limiting
        mapper/              Domain/DTO mapping
        model/               Persistence models
        properties/          Typed configuration properties
        repository/          Repository contracts and implementations
        service/             Business logic contracts and implementations
        util/                Shared utilities
      resources/
        db/migration/        Flyway migrations
        openapi/             Canonical OpenAPI contract
        application*.yaml    Base and profile configuration
    test/
      java/                  Unit and integration tests
      resources/sql/         Database fixtures
  .mvn/wrapper/              Maven Wrapper configuration
  mvnw                       Unix Maven Wrapper
  mvnw.cmd                   Windows Maven Wrapper
  pom.xml                    Dependencies and build configuration
```

## Development

### OpenAPI and generated sources

The canonical OpenAPI 3.1.2 contract is
[`src/main/resources/openapi/arduino-thermo-hygrometer-api.yaml`](src/main/resources/openapi/arduino-thermo-hygrometer-api.yaml).
The Maven `generate-sources` phase creates Spring interfaces and DTOs under
`target/generated-sources/openapi`. Controllers implement those interfaces.

After a contract change, update the [frontend snapshot and types](../app/README.md#openapi-and-generated-types).
Do not edit generated Java or TypeScript files by hand.

### Testing and quality checks

```sh
./mvnw test
./mvnw verify
```

`test` runs unit tests through Surefire. `verify` also runs integration tests through
Failsafe. Both use the `test` Spring profile. Integration tests start their own PostgreSQL
18.3 container using Testcontainers and need a working Docker-compatible runtime; an
already-running local PostgreSQL service is not a substitute.

Spotless checks Java formatting during `validate`. Apply formatting with:

```sh
./mvnw spotless:apply
```

### Building

```sh
./mvnw clean package
```

The application JAR is written to `target/`. Packaging runs unit tests; use `verify` to
include integration tests. `-DskipTests` skips tests when deliberately needed for packaging.

### Database migrations

Flyway migrations live in `src/main/resources/db/migration`, starting with
`V1__initial_migration.sql`. Add a new versioned migration for schema changes.
Do not edit a migration already applied in a shared environment.

## API reference

### Reading endpoints

All reading endpoints require a bearer token with `API_ADMIN`:

| Resource    | Endpoint               | Value field     | Range                 |
| ----------- | ---------------------- | --------------- | --------------------- |
| Temperature | `/api/v1/temperatures` | `temp`          | -55 to 125            |
| Humidity    | `/api/v1/humidities`   | `airHumidity`   | 20 to 90              |
| Battery     | `/api/v1/batteries`    | `batteryStatus` | Integer from 0 to 100 |

Each endpoint supports:

| Method   | Input                                                   | Success response                   |
| -------- | ------------------------------------------------------- | ---------------------------------- |
| `GET`    | `registeredAt` and `dateOnly` query parameters          | HTTP 200 with an array of readings |
| `POST`   | JSON with `registeredAt` and the resource's value field | HTTP 201 with the created reading  |
| `DELETE` | `registeredAt` and `dateOnly` query parameters          | HTTP 204 without a response body   |

For GET and DELETE, both parameters are required. `dateOnly=true` selects the calendar day;
`dateOnly=false` matches the timestamp. Deletion affects all matching records, including
duplicates. No matching readings produces HTTP 404 rather than an empty GET array.

Example date search using an access token returned by `/auth/token`:

```sh
curl --get \
  --header "Authorization: Bearer <access-token>" \
  --data-urlencode "registeredAt=2026-10-19T00:00:00Z" \
  --data-urlencode "dateOnly=true" \
  http://localhost:5000/api/v1/temperatures
```

Example temperature body:

```json
{
  "registeredAt": "2026-10-19T09:30:00Z",
  "temp": 21.5
}
```

Humidity uses the same timestamp field with, for example, `"airHumidity": 55.5`.
Battery uses `"batteryStatus": 75`.

The contract declares `registeredAt` as an ISO date-time. The implementation maps it to
`LocalDateTime`, without an offset, and the Maven run configuration sets the JVM timezone
to `Europe/Oslo`. The frontend explicitly uses UTC. Configure the device and application
to use a consistent timestamp convention; existing offset-free readings cannot establish
their original timezone. Do not assume the API converts offsets to UTC.

### Error responses

Errors use `application/problem+json` and RFC 9457-style fields:
`type`, `title`, `detail`, `status`, `instance`, `traceId`, and `timestamp`.
Validation failures can include an `errors` array identifying invalid values.

| Status | Meaning                                             |
| ------ | --------------------------------------------------- |
| `400`  | Invalid request                                     |
| `401`  | Missing/invalid JWT or rejected sign-in credentials |
| `403`  | Required role is missing                            |
| `404`  | No matching resource/readings                       |
| `429`  | Rate limit exceeded                                 |
| `500`  | Unexpected server error                             |

### Health and documentation

`GET /actuator/health`, `/actuator/health/liveness`, and `/actuator/health/readiness`
are public. Health details are restricted to authorized callers. The exposed `info` and
`metrics` actuator endpoints require `ACTUATOR`.

When enabled by the profile, Swagger UI and `/v3/api-docs` are publicly accessible.
They are disabled in staging and production.

## Deployment

Use the `staging` or `prod` profile with externally supplied JWT settings and datasource
credentials, HTTPS, and the intended CORS origins. Do not deploy with the checked-in sample
database credentials. Protect the signing secret, use a suitable short token lifetime,
and persist PostgreSQL data outside disposable containers.

## Related documentation

- [Repository overview](../README.md)
- [Frontend guide](../app/README.md)
- [GitHub configuration](../.github/README.md)
- [CI workflows and current limitations](../.github/workflows/README.md)

## License

MIT. See [LICENSE](../LICENSE).
