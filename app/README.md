# Arduino Thermo Hygrometer Web UI

A React 19 + TypeScript dashboard for the Arduino Thermo Hygrometer REST API. The UI uses Chakra UI for presentation,
SWR for client-side caching and revalidation, openapi-fetch for type-safe HTTP requests, openapi-typescript for
contract-generated types, React Router data routers for route loaders, and Vite/Vitest for development and testing.

## Prerequisites

- Node.js 24.18+
- npm 11.15+
- The API running locally on `http://localhost:5000` for development data

The Node/npm requirements match the engines already declared by this repository. Do not downgrade them when running the
frontend.

## Run the frontend

From the `app` folder:

```bash
npm install
npm run generate-types
npm run dev
```

The development server runs at `http://localhost:3000/arduinothermohygrometer/` using the existing Vite base-path
configuration. API base URLs, the API header name, and the local API key are read from Vite environment variables. Keep
local secrets in `.env.local` and never commit production credentials.

## Useful scripts

```bash
npm run dev
npm run generate-types
npm run test:run
npm run lint
npm run format:check
npm run build:dev
```

`generate-types` regenerates `src/arduino-thermo-hygrometer-api.d.ts` from the OpenAPI document. The generated file is
kept in source control so the frontend has a usable contract immediately after checkout; regenerate it whenever the
specification changes.

## Structure

```text
src/
├── components/          # Chakra UI presentation components and route error UI
├── hooks/               # Specialized SWR hooks by resource and mutation
├── lib/
│   ├── api/             # openapi-fetch client and one module per API resource
│   └── date/            # Backend-compatible date/time helpers
├── pages/               # Route-level pages with colocated loaders and tests
├── types/               # Domain aliases and form types
├── AppProvider.tsx      # Chakra + SWR application providers
├── Main.tsx             # React entry point
└── Routes.tsx           # React Router data-router definition
```

Every top-level source folder has an `index.ts` barrel. React components and pages use PascalCase `.tsx` names;
TypeScript modules use lowercase `.ts` names. Tests live beside the implementation using `Xxxx.component.test.tsx` and
`Xxxx.page.test.tsx` naming.

## Data flow

The OpenAPI document is the source of truth for request and response types. `openapi-typescript` generates the `paths`
and `component` types, `openapi-fetch` provides the typed HTTP boundary, and specialized resource modules wrap each
endpoint. SWR hooks then own cache keys, revalidation, and mutations at the React boundary.

React Router is created once outside the React tree with `createBrowserRouter` and rendered with `RouterProvider`.
Dashboard and history routes use `loader()` to parse URL state and load their initial data before rendering. SWR
receives that loader data as fallback data and takes over cache-based revalidation after navigation. This keeps
navigation state and server state separate while avoiding duplicate initial requests.

The history page enables only the selected resource's SWR hook, so a temperature history request does not also fetch
battery and humidity data. Creation uses one specialized SWR mutation hook per resource, and deletion uses a dedicated
mutation boundary that delegates to the resource-specific API modules.

## Error handling

The backend follows RFC 9457-style problem details with concise status-specific messages. Transport errors from
`openapi-fetch` are converted into user-oriented messages, while validation errors can display the backend's field-level
messages. React Router's `ErrorBoundary` is also a dedicated component so route loader failures do not fall through to
an unhelpful blank screen.

## Styling

The application uses Chakra UI primitives and recipes throughout. There is no custom stylesheet; native HTML is used
only where Chakra intentionally wraps a browser control, such as date/time and numeric inputs.

## Frontend architecture

The application uses React Router Data Mode for URL-driven data loading and route-level error handling. Route
definitions live in `src/routes.ts`; page implementations are lazy-loaded with `route.lazy`, while the root layout and
fallback page remain eagerly available.

Route loaders fetch initial measurement data with the typed `openapi-fetch` client and seed the matching SWR cache. The
page hooks consume those cache keys and own subsequent revalidation. Mutations invalidate the affected sensor/date key
so the next view receives fresh data. This keeps initial navigation owned by the router while keeping reusable server
state owned by SWR.

API access is separated into specialized endpoint modules, then consumed through specialized hooks. Generated OpenAPI
types remain the source of truth for API DTOs. React components use Chakra UI primitives, with browser elements used
only where they provide a native control such as date and number inputs.
