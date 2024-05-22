type Environment = "Development" | "Test" | "Production";

interface AppConfiguration {
  environment: Environment;
  buildId?: string;
  APIBaseURL?: string;
  defaultLanguage: string;
  fallbackLanguage: string;
}

export const appConfiguration: AppConfiguration = {
  environment: import.meta.env.VITE_ENVIRONMENT as Environment,
  buildId: import.meta.env.VITE_BUILDID ?? "Development",
  APIBaseURL:
    import.meta.env.VITE_APIBASEURL ?? `${window.location.protocol}//${window.location.host}`,
  defaultLanguage: "nb",
  fallbackLanguage: "nb",
};
