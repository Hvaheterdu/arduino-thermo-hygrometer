import viteReact from "@vitejs/plugin-react";
import { ConfigEnv, loadEnv } from "vite";
import { defineConfig, type Plugin } from "vitest/config";

const buildContentSecurityPolicy = (apiBaseUrl: string, allowInlineScripts: boolean): string => {
  const apiOrigin = URL.canParse(apiBaseUrl) ? new URL(apiBaseUrl).origin : "'self'";
  const connectSource = apiOrigin === "'self'" ? "'self'" : `'self' ${apiOrigin}`;
  const scriptSource = allowInlineScripts ? "'self' 'unsafe-inline'" : "'self'";
  const directives = [
    "base-uri 'self'",
    `connect-src ${connectSource}`,
    "default-src 'self'",
    "form-action 'self'",
    "frame-ancestors 'none'",
    "img-src 'self' data:",
    "manifest-src 'none'",
    "object-src 'none'",
    `script-src ${scriptSource}`,
    "style-src 'self' 'unsafe-inline'",
    "worker-src 'self'"
  ];

  return directives.join("; ");
};

const buildSecurityHeaders = (apiBaseUrl: string, allowInlineScripts: boolean): Record<string, string> => {
  return {
    "x-content-type-options": "nosniff",
    "referrer-policy": "no-referrer",
    "permissions-policy":
      "accelerometer=(), autoplay=(), bluetooth=(), camera=(), compute-pressure=(), cross-origin-isolated=(), display-capture=(), encrypted-media=(), fullscreen=(), geolocation=(), gyroscope=(), hid=(), identity-credentials-get=(), idle-detection=(), magnetometer=(), microphone=(), midi=(), payment=(), picture-in-picture=(), publickey-credentials-create=(), publickey-credentials-get=(), screen-wake-lock=(), storage-access=(), sync-xhr=(), usb=(), web-share=(), window-management=(), xr-spatial-tracking=()",
    "content-security-policy": buildContentSecurityPolicy(apiBaseUrl, allowInlineScripts)
  };
};

const contentSecurityPolicyPlugin = (apiBaseUrl: string, allowInlineScripts: boolean): Plugin => ({
  name: "content-security-policy",
  transformIndexHtml: {
    order: "pre",
    handler: () => [
      {
        tag: "meta",
        attrs: {
          "http-equiv": "Content-Security-Policy",
          content: buildContentSecurityPolicy(apiBaseUrl, allowInlineScripts)
        },
        injectTo: "head-prepend"
      }
    ]
  }
});

export default defineConfig(({ command, mode }: ConfigEnv) => {
  const env = loadEnv(mode, import.meta.dirname, "");
  const isLocal = command === "serve";
  let apiBaseUrl = "";
  if (isLocal) {
    apiBaseUrl = env.VITE_API_BASEURL_LOCAL;
  } else {
    switch (mode) {
      case "production":
        apiBaseUrl = env.VITE_API_BASEURL_PRODUCTION;
        break;
      case "staging":
        apiBaseUrl = env.VITE_API_BASEURL_STAGING;
        break;
      case "development":
      default:
        apiBaseUrl = env.VITE_API_BASEURL_DEVELOPMENT;
        break;
    }
  }
  const allowInlineScripts = isLocal || mode === "development";
  const securityHeaders = buildSecurityHeaders(apiBaseUrl, allowInlineScripts);
  const plugins = [viteReact(), contentSecurityPolicyPlugin(apiBaseUrl, allowInlineScripts)];

  return {
    base: env.VITE_BASE_PATH,
    build: {
      outDir: "dist",
      sourcemap: mode !== "production",
      reportCompressedSize: true
    },
    cacheDir: "node_modules/.vite/app",
    plugins,
    preview: {
      host: "localhost",
      port: 4173,
      strictPort: true,
      headers: securityHeaders
    },
    resolve: {
      tsconfigPaths: true
    },
    root: import.meta.dirname,
    test: {
      clearMocks: true,
      environment: "jsdom",
      globals: true,
      include: ["src/**/*.test.{ts,tsx}"],
      setupFiles: ["./src/test/setup.ts"]
    },
    server: {
      host: "localhost",
      port: 3000,
      strictPort: true,
      headers: securityHeaders
    }
  };
});
