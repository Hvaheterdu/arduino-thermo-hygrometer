# Web UI

Frontend application built with React, TypeScript, and Vite.

## Prerequisites

- Node.js 24+
- npm 11+

## Local Development

Run from this folder:

```bash
npm ci
npm run app:dev
```

## Quality Scripts

- `npm run app:format`: format files with Prettier
- `npm run app:format:check`: check formatting without writing changes
- `npm run app:lint`: run ESLint
- `npm run app:build:dev`: build in development mode and run TypeScript checks
- `npm run app:test:run`: run Vitest unit and component tests
- `npm run app:budget`: enforce bundle size budget for generated `dist/assets` files
- `npm run app:test:e2e`: run Playwright smoke and accessibility tests

Recommended local validation before pushing:

```bash
npm run app:format && npm run app:lint && npm run app:build:dev && npm run app:budget && npm run app:test:run && npm run app:test:e2e
```

## End-to-End Tests

Install browser binaries once:

```bash
npx playwright install chromium
```

E2E tests run against Vite preview and are configured in `playwright.config.ts`.

## Environment Variables

Set these in `.env.local` as needed:

- `VITE_ENVIRONMENT`: active API environment (`local`, `staging`, `production`)
- `VITE_APIBASEURL`, `VITE_APIBASEURL_LOCAL`, `VITE_APIBASEURL_STAGING`, `VITE_APIBASEURL_PRODUCTION`: API base URLs
- `VITE_APIKEY`: optional API key sent as `X-API-KEY`
- `VITE_DEV_HTTPS`: `true`/`false` for optional HTTPS in local dev (requires cert files)
- `VITE_API_TIMEOUT_MS`: API request timeout in milliseconds (clamped to 1000-30000, default 10000)
- `VITE_API_RETRY_ATTEMPTS`: retry attempts for idempotent API GET failures (clamped to 0-3, default 2)

## CI Expectations

Frontend CI is defined in `.github/workflows/frontend-ci.yml` and runs on frontend-related changes.

It executes these checks:

1. `npm run app:format:check`
2. `npm run app:lint`
3. `npm run app:build:dev`
4. `npm run app:budget`
5. `npm run app:test:run`
6. `npm run app:test:e2e`
