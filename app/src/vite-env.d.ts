/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_ENVIRONMENT?: string;
  readonly VITE_APIBASEURL?: string;
  readonly VITE_APIBASEURL_LOCAL?: string;
  readonly VITE_APIBASEURL_STAGING?: string;
  readonly VITE_APIBASEURL_PRODUCTION?: string;
  readonly VITE_APIKEY?: string;
  readonly VITE_API_TIMEOUT_MS?: string;
  readonly VITE_API_RETRY_ATTEMPTS?: string;
  readonly VITE_DEV_HTTPS?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
