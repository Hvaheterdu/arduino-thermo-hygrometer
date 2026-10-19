import react from "@vitejs/plugin-react";
import { msw } from "msw/vite";
import { loadEnv } from "vite";
import { defineConfig } from "vitest/config";

export default defineConfig(({ command, mode, isPreview }) => {
  const env = loadEnv(mode, import.meta.dirname, "VITE_");
  const isDevServer = command === "serve" && !isPreview;
  const apiUrl = isDevServer ? env.VITE_API_BASEURL_LOCAL : env[`VITE_API_BASEURL_${mode.toUpperCase()}`];
  const apiOrigin = apiUrl && URL.canParse(apiUrl) ? new URL(apiUrl).origin : "";
  const headers = {
    "X-Content-Type-Options": "nosniff",
    "Referrer-Policy": "no-referrer",
    "Permissions-Policy": "camera=(), microphone=(), geolocation=()",
    "Content-Security-Policy": [
      "default-src 'self'",
      "base-uri 'self'",
      "object-src 'none'",
      "frame-ancestors 'none'",
      "form-action 'self'",
      `script-src 'self'${isDevServer ? " 'unsafe-inline'" : ""}`,
      "style-src 'self' 'unsafe-inline'",
      "img-src 'self' data:",
      `connect-src 'self' ${apiOrigin}${isDevServer ? " ws://localhost:3000" : ""}`,
      "worker-src 'self'"
    ].join("; ")
  };

  return {
    base: env.VITE_BASE_PATH,
    plugins: [react(), ...(isDevServer ? [msw({ mode: "worker-only" })] : [])],
    resolve: { tsconfigPaths: true },
    build: { target: "es2023", sourcemap: mode !== "production" },
    server: { host: "localhost", port: 3000, strictPort: true, headers },
    preview: { host: "localhost", headers },
    test: {
      environment: "jsdom",
      globals: false,
      clearMocks: true,
      restoreMocks: true,
      include: ["src/**/*.test.{ts,tsx}"],
      setupFiles: ["./src/test/test.setup.ts"],
      env: {
        VITE_MOCK_BACKEND: "false",
        VITE_API_BASEURL_LOCAL: "http://localhost:5000"
      }
    }
  };
});
