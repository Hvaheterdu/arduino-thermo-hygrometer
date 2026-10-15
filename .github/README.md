# GitHub configuration

This directory contains repository ownership, automated dependency update, and CI/security workflow configuration.

## Files

- [`CODEOWNERS`](CODEOWNERS) assigns the repository owner to all files, including the `api/` and `app/` directories.
- [`dependabot.yml`](dependabot.yml) configures scheduled updates for GitHub Actions, npm, and Maven dependencies.
- [`workflows/`](workflows/README.md) documents the backend, frontend, and CodeQL GitHub Actions workflows.

Dependabot's npm entry currently targets the repository root (`/`); the frontend package manifest is in `app/`. Review
that directory setting if npm dependency updates are expected for the frontend.
