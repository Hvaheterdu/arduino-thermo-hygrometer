# Arduino Thermo Hygrometer

A small IoT project for collecting temperature, air-humidity, and battery measurements from an Arduino thermo hygrometer.
The repository contains a Spring Boot REST API and a React web application. The frontend is currently a development
scaffold; the API and OpenAPI contract provide the measurement endpoints.

## Repository

- [`api/`](api/README.md) — Java 25, Spring Boot REST API, PostgreSQL, and Flyway migrations.
- [`app/`](app/README.md) — React and TypeScript frontend built with Vite.
- [`.github/`](.github/README.md) — repository ownership, dependency update, and CI workflow configuration.

## Run locally

Start PostgreSQL and the API by following the [API setup guide](api/README.md#local-database). Then start the frontend:

```bash
cd app
npm ci
npm run dev
```

The frontend development server runs at `http://localhost:3000`. Its local API URL defaults to
`http://localhost:5000`; see [`app/.env.example`](app/.env.example) for the Vite environment variables.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
