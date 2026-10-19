/// <reference types="vite/client" />
interface ImportMetaEnv {
    readonly VITE_API_BASEURL_LOCAL: string;
    readonly VITE_API_BASEURL_DEVELOPMENT: string;
    readonly VITE_API_BASEURL_STAGING: string;
    readonly VITE_API_BASEURL_PRODUCTION: string;
    readonly VITE_BASE_PATH: string;
    readonly VITE_MOCK_BACKEND: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
