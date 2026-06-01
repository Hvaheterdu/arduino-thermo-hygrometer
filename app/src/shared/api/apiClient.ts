import createClient, { type Middleware } from "openapi-fetch";
import { frontendLogger } from "../observability/logger";
import type { ApiPaths } from "./apiSchema";

const defaultApiBaseUrl = "http://localhost:5000";
const defaultApiTimeoutMilliseconds = 10_000;
const minimumApiTimeoutMilliseconds = 1_000;
const maximumApiTimeoutMilliseconds = 30_000;

interface ApiBaseUrlConfiguration {
  readonly environment: string;
  readonly fallbackBaseUrl: string | undefined;
  readonly localBaseUrl: string | undefined;
  readonly stagingBaseUrl: string | undefined;
  readonly productionBaseUrl: string | undefined;
}

const normalizeEnvironment = (value: string): "local" | "staging" | "production" => {
  if (value === "staging") {
    return "staging";
  }

  if (value === "production") {
    return "production";
  }

  return "local";
};

const resolveBaseUrl = (rawUrl: string | undefined): string => {
  if (!rawUrl) {
    return defaultApiBaseUrl;
  }

  try {
    const parsed = new URL(rawUrl);
    if (parsed.protocol === "http:" || parsed.protocol === "https:") {
      return parsed.toString().replace(/\/$/, "");
    }
  } catch {
    return defaultApiBaseUrl;
  }

  return defaultApiBaseUrl;
};

const resolveApiBaseUrlByEnvironment = (configuration: ApiBaseUrlConfiguration): string => {
  const environment = normalizeEnvironment(configuration.environment.toLowerCase());

  const environmentUrl =
    environment === "local" ? configuration.localBaseUrl
    : environment === "staging" ? configuration.stagingBaseUrl
    : configuration.productionBaseUrl;

  return resolveBaseUrl(environmentUrl ?? configuration.fallbackBaseUrl);
};

const resolveApiTimeoutMilliseconds = (value: string | undefined): number => {
  const parsedTimeout = Number(value);

  if (!Number.isFinite(parsedTimeout)) {
    return defaultApiTimeoutMilliseconds;
  }

  return Math.min(maximumApiTimeoutMilliseconds, Math.max(minimumApiTimeoutMilliseconds, parsedTimeout));
};

const requestTimeoutControllerByRequest = new WeakMap<Request, AbortController>();
const requestTimeoutHandleByRequest = new WeakMap<Request, ReturnType<typeof setTimeout>>();

const clearRequestTimeout = (request: Request): void => {
  const timeoutHandle = requestTimeoutHandleByRequest.get(request);

  if (timeoutHandle !== undefined) {
    clearTimeout(timeoutHandle);
    requestTimeoutHandleByRequest.delete(request);
  }

  requestTimeoutControllerByRequest.delete(request);
};

const authMiddleware: Middleware = {
  onRequest({ request }) {
    const apiTimeoutInput =
      typeof import.meta.env.VITE_API_TIMEOUT_MS === "string" ? import.meta.env.VITE_API_TIMEOUT_MS : undefined;

    const timeoutController = new AbortController();
    const timeoutHandle = setTimeout(() => {
      timeoutController.abort();
    }, resolveApiTimeoutMilliseconds(apiTimeoutInput));

    requestTimeoutControllerByRequest.set(request, timeoutController);
    requestTimeoutHandleByRequest.set(request, timeoutHandle);

    const timedRequest = new Request(request, {
      signal: timeoutController.signal
    });

    timedRequest.headers.set("Accept", "application/json");

    const apiKey = import.meta.env.VITE_APIKEY;
    if (typeof apiKey === "string" && apiKey.trim().length > 0) {
      timedRequest.headers.set("X-API-KEY", apiKey);
    }

    return timedRequest;
  },
  onResponse({ request, response }) {
    clearRequestTimeout(request);

    if (!response.ok) {
      frontendLogger.warn("API response returned non-success status code.", {
        url: request.url,
        method: request.method,
        status: response.status,
        statusText: response.statusText
      });
    }

    return response;
  },
  onError({ error, request }) {
    clearRequestTimeout(request);

    frontendLogger.error("API request failed before response completion.", {
      url: request.url,
      method: request.method,
      errorName: error instanceof Error ? error.name : "UnknownError",
      errorMessage: error instanceof Error ? error.message : "Unknown API request error"
    });

    return error instanceof Error ? error : new Error(String(error));
  }
};

export const apiClient = createClient<ApiPaths>({
  baseUrl: resolveApiBaseUrlByEnvironment({
    environment: import.meta.env.VITE_ENVIRONMENT ?? import.meta.env.MODE,
    fallbackBaseUrl: import.meta.env.VITE_APIBASEURL,
    localBaseUrl: import.meta.env.VITE_APIBASEURL_LOCAL,
    stagingBaseUrl: import.meta.env.VITE_APIBASEURL_STAGING,
    productionBaseUrl: import.meta.env.VITE_APIBASEURL_PRODUCTION
  })
});

apiClient.use(authMiddleware);
