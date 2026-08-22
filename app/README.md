# Arduino Thermo Hygrometer Web UI

A small React + TypeScript dashboard for the Arduino Thermo Hygrometer REST API. The UI uses Chakra UI for accessible
presentation, SWR for client-side caching/revalidation, openapi-fetch for type-safe HTTP requests, openapi-typescript
for generated API types, React Router for navigation, and Vite/Vitest for development and testing.

## Prerequisites

- Node.js 24+
- npm 11+
- The API running locally on `http://localhost:5000` for development data

## Run the frontend

From the `app` folder:

```bash
npm install
npm run generate-types
npm run dev
```

The development server runs at `http://localhost:3000/arduinothermohygrometer/` with the current local base path
configuration.

The API key and API base URL are read from Vite environment variables. Keep secrets in `.env.local` and do not commit
them. The frontend expects `VITE_API_KEY`, `VITE_API_HEADER_NAME`, and an environment-specific API URL such as
`VITE_API_BASEURL_DEVELOPMENT`.

## Available scripts

```bash
npm run dev
npm run test:run
npm run lint
npm run format:check
npm run build:dev
```

`generate-types` regenerates `src/types/api/arduino-thermo-hygrometer-api.d.ts` from the OpenAPI document.

## Application structure

The source is organised by feature. `features/dashboard` contains the dashboard page and its UI, `features/history`
contains historical reading queries and presentation, `shared` contains API infrastructure and reusable UI, and `types`
contains generated API types plus feature/domain types.
