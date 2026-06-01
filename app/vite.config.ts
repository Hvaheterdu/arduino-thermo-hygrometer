import viteReact from "@vitejs/plugin-react";
import fs from "node:fs";
import path from "node:path";
import { checker } from "vite-plugin-checker";
import vitePluginSvgr from "vite-plugin-svgr";
import { defineConfig } from "vitest/config";

const certDirectory = path.resolve(import.meta.dirname, "../.cert");
const devKeyPath = path.resolve(certDirectory, "dev.pem");
const devCertPath = path.resolve(certDirectory, "cert.pem");
const hasDevCertificates = fs.existsSync(devKeyPath) && fs.existsSync(devCertPath);

const toBoolean = (value: string | undefined): boolean => {
  return value === "true" || value === "1";
};

const buildBaseSecurityHeaders = (): Record<string, string> => {
  return {
    "x-content-type-options": "nosniff",
    "x-frame-options": "DENY",
    "referrer-policy": "strict-origin-when-cross-origin",
    "permissions-policy":
      "accelerometer=(), camera=(), geolocation=(), gyroscope=(), microphone=(), payment=(), usb=()",
    "cross-origin-opener-policy": "same-origin",
    "cross-origin-resource-policy": "same-site",
    "x-permitted-cross-domain-policies": "none"
  };
};

const buildDevelopmentServerSecurityHeaders = (isHttpsEnabledForDevelopmentServer: boolean): Record<string, string> => {
  const developmentServerSecurityHeaders: Record<string, string> = {
    ...buildBaseSecurityHeaders(),
    "cross-origin-opener-policy": "same-origin",
    "cross-origin-embedder-policy": "require-corp",
    "cross-origin-resource-policy": "same-site",
    "content-security-policy":
      "default-src 'self'; base-uri 'self'; frame-ancestors 'none'; object-src 'none'; " +
      "script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline'; " +
      "img-src 'self' data: blob:; font-src 'self' data:; connect-src 'self' http: https: ws: wss:"
  };

  if (isHttpsEnabledForDevelopmentServer) {
    developmentServerSecurityHeaders["strict-transport-security"] = "max-age=31536000; includeSubDomains";
  }

  return developmentServerSecurityHeaders;
};

const buildPreviewSecurityHeaders = (): Record<string, string> => {
  return {
    ...buildBaseSecurityHeaders(),
    "cross-origin-embedder-policy": "credentialless",
    "content-security-policy":
      "default-src 'self'; base-uri 'self'; frame-ancestors 'none'; object-src 'none'; " +
      "script-src 'self'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; " +
      "font-src 'self' data:; connect-src 'self' https:"
  };
};

export default defineConfig(({ command }) => {
  const isHttpsEnabledForDevelopmentServer =
    command === "serve" && toBoolean(process.env.VITE_DEV_HTTPS) && hasDevCertificates;

  const plugins = [
    checker({
      overlay: {
        initialIsOpen: true,
        position: "br"
      },
      typescript: {
        tsconfigPath: "./tsconfig.json",
        root: import.meta.dirname
      },
      eslint: {
        useFlatConfig: true,
        lintCommand: `eslint ${import.meta.dirname}`
      }
    }),
    viteReact(),
    vitePluginSvgr({
      include: "**/*.svg"
    })
  ];

  return {
    root: import.meta.dirname,
    plugins,
    build: {
      outDir: "dist",
      sourcemap: true,
      reportCompressedSize: true
    },
    test: {
      include: ["src/tests/**/*Test.{ts,tsx}"],
      environment: "node",
      globals: true,
      clearMocks: true
    },
    cacheDir: "node_modules/.vite/app",
    server: {
      https:
        isHttpsEnabledForDevelopmentServer ?
          {
            key: fs.readFileSync(devKeyPath),
            cert: fs.readFileSync(devCertPath)
          }
        : undefined,
      host: "localhost",
      port: 3000,
      strictPort: true,
      headers: buildDevelopmentServerSecurityHeaders(isHttpsEnabledForDevelopmentServer)
    },
    preview: {
      host: "localhost",
      port: 4173,
      strictPort: true,
      headers: buildPreviewSecurityHeaders()
    }
  };
});
