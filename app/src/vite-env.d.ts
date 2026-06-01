// src/vite-env.d.ts
/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_API_BASEURL?: string;
  readonly VITE_API_BASEURL_LOCAL?: string;
  readonly VITE_API_BASEURL_PRODUCTION?: string;
  readonly VITE_API_BASEURL_STAGING?: string;
  readonly VITE_API_KEY?: string;
  readonly VITE_API_RETRY_ATTEMPTS?: string;
  readonly VITE_API_TIMEOUT_MS?: string;
  readonly VITE_ENVIRONMENT?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
