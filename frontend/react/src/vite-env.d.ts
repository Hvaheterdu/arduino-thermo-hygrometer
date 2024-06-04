/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_ENVIRONMENT: string;
  readonly VITE_APIBASEURL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
