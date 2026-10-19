# Arduino Thermo Hygrometer GitHub Actions

Continuous integration for the API and frontend, plus scheduled CodeQL analysis.

## Overview

| Workflow                       | Triggers                                                             | Main checks                                       |
| ------------------------------ | -------------------------------------------------------------------- | ------------------------------------------------- |
| [Backend CI](backend-ci.yml)   | Pushes to `main` and pull requests affecting `api/` or this workflow | Maven verification and integration tests          |
| [Frontend CI](frontend-ci.yml) | Pushes to `main` and pull requests affecting `app/` or this workflow | Formatting, Oxlint, development build, and Vitest |
| [CodeQL](codeql.yml)           | Pushes and pull requests targeting `main`; Mondays at 08:00 UTC      | Java/Kotlin and JavaScript/TypeScript analysis    |

## Requirements

GitHub Actions must be enabled. CI jobs use Ubuntu runners; CodeQL requests code-scanning
upload and repository/package read permissions. Backend CI configures Java 25, and frontend
CI configures Node.js 24. See the application guides for local runtime requirements.

## Getting started

Open a pull request with changes matching the workflow's path filters, or push matching
changes to `main`. Check results in the pull request or the repository's Actions tab.
There is no manual `workflow_dispatch` trigger.

The application guides describe [API checks](../../api/README.md#testing-and-quality-checks)
and [frontend checks](../../app/README.md#testing-and-quality-checks) that can be run locally.

## Configuration

### Backend CI

[`backend-ci.yml`](backend-ci.yml) uses the Maven Wrapper from `api/`:

| Job                 | Command                            | Notes                                                                            |
| ------------------- | ---------------------------------- | -------------------------------------------------------------------------------- |
| `backend-quality`   | `./mvnw -B verify -DskipITs`       | Runs formatting, unit tests, and verification without Failsafe integration tests |
| `integration-tests` | `./mvnw -B verify -DskipUnitTests` | Runs after the quality job and provisions a PostgreSQL 18.3 service              |

Two details matter when interpreting this workflow:

- `skipUnitTests` is not connected to Surefire's skip setting in the current `pom.xml`,
  so the second job does not reliably separate unit tests from integration tests.
- Integration tests use their own PostgreSQL Testcontainers instance through
  `TestcontainerManager` and `@ServiceConnection`. The workflow's PostgreSQL service and
  datasource environment variables do not replace that container requirement.

### Frontend CI

[`frontend-ci.yml`](frontend-ci.yml) caches dependencies using `app/package-lock.json`,
runs `npm ci`, and executes:

```sh
npm run format:check && npm run lint && npm run build:dev
npm run test:run
```

`lint` runs type-aware Oxlint. `build:dev` includes the TypeScript build, so type checking
is covered even without a separate `typecheck` step. The development-mode bundle still
excludes MSW; tests use Node request interception.

The workflow currently does **not** install the separately locked OpenAPI generator or
run `generate-types:check`. It also does not run the production build. Backend-only
contract changes do not match the frontend workflow's path filters.
Local contract verification requires:

```sh
cd app
npm run setup:types
npm run generate-types:check
```

Adding generator installation, its lockfile cache, a drift check, production-build coverage,
and the canonical contract path to CI are follow-up workflow changes, not existing checks.

### CodeQL

[`codeql.yml`](codeql.yml) analyzes `java-kotlin` with `autobuild` and
`javascript-typescript` with `build-mode: none`, then uploads results to GitHub code scanning.
It runs weekly at `00 08 * * 1`, in addition to its push/pull-request triggers.

Unlike Backend CI, this workflow does not explicitly install Java 25. If Java autobuild
fails the Maven Enforcer requirement, configure the appropriate toolchain in the workflow
rather than weakening the application's Java requirement.

## Project structure

```text
.github/workflows/
  backend-ci.yml        API quality and integration-test jobs
  frontend-ci.yml       Frontend quality and test job
  codeql.yml            Code-scanning matrix and schedule
  README.md            Workflow behavior and limitations
```

## Development

Keep path filters, runtime setup, dependency caches, and script names aligned with
[`api/pom.xml`](../../api/pom.xml), [`app/package.json`](../../app/package.json), and
[`app/tools/openapi/package.json`](../../app/tools/openapi/package.json).
README descriptions distinguish current behavior from improvements that still require
workflow edits. No workflow YAML is changed by this documentation revision.

## Related documentation

- [Repository overview](../../README.md)
- [API guide](../../api/README.md)
- [Frontend guide](../../app/README.md)
- [GitHub configuration](../README.md)

## License

MIT. See [LICENSE](../../LICENSE).
