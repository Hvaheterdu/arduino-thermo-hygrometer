# GitHub Actions

These workflows run the repository's continuous-integration checks and CodeQL analysis.

## Backend CI

[`backend-ci.yml`](backend-ci.yml) runs on pushes to `main` and pull requests when `api/` or the workflow file changes.
It uses Java 25 and the Maven Wrapper. The backend quality job runs Maven verification; the integration-test job runs
after it with PostgreSQL 18.3 available as a service.

## Frontend CI

[`frontend-ci.yml`](frontend-ci.yml) runs on pushes to `main` and pull requests when `app/` or the workflow file
changes. It uses Node.js 24, installs from the lockfile with `npm ci`, and runs formatting checks, linting, a development
build, and the frontend test suite.

## CodeQL

[`codeql.yml`](codeql.yml) analyzes Java/Kotlin and JavaScript/TypeScript on pushes and pull requests targeting `main`.
It also runs weekly on Mondays at 08:00 UTC and uploads its findings to GitHub code scanning.
