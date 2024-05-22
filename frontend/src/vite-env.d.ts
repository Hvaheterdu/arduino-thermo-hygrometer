/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_ENVIRONMENT: string;
  readonly VITE_BUILDID: string;
  readonly VITE_APIBASEURL: string;
  readonly VITE_APIAUTHCONFIG: string;
  readonly VITE_CLIENTID: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
