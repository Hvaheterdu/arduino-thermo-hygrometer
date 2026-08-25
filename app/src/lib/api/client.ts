import createClient from "openapi-fetch";

import type { paths } from "../../arduino-thermo-hygrometer-api";

const environmentKey = (mode: string): string => `VITE_API_BASEURL_${mode.toUpperCase()}`,
  getApiBaseUrl = (): string => {
    const modeKey: keyof ImportMetaEnv = environmentKey(import.meta.env.MODE) as keyof ImportMetaEnv,
      modeSpecific: string | undefined = import.meta.env[modeKey],
      development: string = import.meta.env.VITE_API_BASEURL_DEVELOPMENT;

    return modeSpecific || development;
  },
  apiKey: string | undefined = import.meta.env.VITE_API_KEY,
  apiHeaderName: string = import.meta.env.VITE_API_HEADER_NAME || "X-API-KEY";

export const apiClient = createClient<paths>({
  baseUrl: getApiBaseUrl(),
  ...(apiKey === undefined
    ? {}
    : {
        headers: {
          [apiHeaderName]: apiKey
        }
      })
});
