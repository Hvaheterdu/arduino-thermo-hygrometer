# Arduino Thermo Hygrometer Web UI

A React 19 + TypeScript dashboard for the Arduino Thermo Hygrometer REST API. The UI uses Chakra UI for presentation,
SWR for client-side caching and revalidation, openapi-fetch for type-safe HTTP requests, openapi-typescript for
generated API types, React Router data routers for loaders/actions, and Vite/Vitest for development and testing.

## Prerequisites

- Node.js 24.18+
- npm 11.15+
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

The API key and API base URL are read from Vite environment variables. Keep local values in `.env.local` and do not
commit secrets. The frontend expects `VITE_API_KEY`, `VITE_API_HEADER_NAME`, and an environment-specific API URL such as
`VITE_API_BASEURL_DEVELOPMENT`.

## Available scripts

```bash
npm run dev
npm run generate-types
npm run test:run
npm run lint
npm run format:check
npm run build:dev
```

`generate-types` regenerates `src/arduino-thermo-hygrometer-api.d.ts` from the OpenAPI document.

## Application structure

- `components/` contains reusable Chakra UI presentation components and error boundaries.
- `pages/` contains route-level pages and their colocated tests.
- `hooks/` contains specialized SWR hooks for temperature, humidity, battery, and deletion operations.
- `lib/api/` contains the single configured openapi-fetch client plus specialized endpoint calls and API error mapping.
- `lib/date/` contains date/time conversion and presentation helpers.
- `types/` contains domain types and aliases for generated OpenAPI types.
- `routes.ts` defines the React Router data router; route loaders/actions stay colocated with their pages.

The dashboard and history pages use React Router `loader()` functions to derive query state from the URL. Network data
is fetched through specialized SWR hooks, keeping transport concerns outside page components. Creation uses a route
`action`
so form submission, navigation, and the `NotCreated` error boundary are handled by the data router.

## Frontend architecture notes

The frontend keeps React Router route concerns close to their pages:

- `pages/<page>/<Page>.page.tsx` contains the UI.
- `pages/<page>/<page>.loader.ts` contains route-specific loader parsing.
- `pages/create/create.action.ts` contains the create mutation used by the route action.
- `lib/api/<resource>.ts` owns the typed OpenAPI calls for one backend resource.
- `hooks/` contains specialized SWR hooks for independently reusable server-state behavior.

The top-level `routes.ts` intentionally remains a small route registry. React Router data APIs own navigation-time
loading and mutations, while SWR owns client-side server-state fetching and cache revalidation.
