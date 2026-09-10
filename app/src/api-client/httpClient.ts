import createClient, { type Middleware } from "openapi-fetch";

import type { paths } from "@/arduino-thermo-hygrometer-api";
import { getApiBaseUrl, getApiKey, getApiKeyHeaderName } from "@/utils/env.util";

const apiKeyMiddleware: Middleware = {
  onRequest({ request }) {
    const apiKey = getApiKey();
    if (apiKey) {
      request.headers.set(getApiKeyHeaderName(), apiKey);
    }

    return request;
  }
};

export const httpClient = createClient<paths>({ baseUrl: getApiBaseUrl() });

httpClient.use(apiKeyMiddleware);
