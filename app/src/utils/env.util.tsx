export const getApiBaseUrl = (): string => {
  if (import.meta.env.DEV) {
    return import.meta.env.VITE_API_BASEURL_LOCAL;
  }

  switch (import.meta.env.MODE) {
    case "staging":
      return import.meta.env.VITE_API_BASEURL_STAGING;
    case "production":
      return import.meta.env.VITE_API_BASEURL_PRODUCTION;
    case "development":
    default:
      return import.meta.env.VITE_API_BASEURL_DEVELOPMENT;
  }
};

export const getApiKey = (): string => import.meta.env.VITE_API_KEY;

export const getApiKeyHeaderName = (): string => import.meta.env.VITE_API_HEADER_NAME;

export const getBasePath = (): string => import.meta.env.VITE_BASE_PATH || "/";
