import viteReact from "@vitejs/plugin-react";
import { checker } from "vite-plugin-checker";
import vitePluginSvgr from "vite-plugin-svgr";
import { defineConfig } from "vitest/config";

const buildSecurityHeaders = (): Record<string, string> => {
  return {
    "x-content-type-options": "nosniff",
    "referrer-policy": "no-referrer",
    "permissions-policy":
      "accelerometer=(), autoplay=(), bluetooth=(), camera=(), compute-pressure=(), cross-origin-isolated=(), display-capture=(), encrypted-media=(), fullscreen=(), geolocation=(), gyroscope=(), hid=(), identity-credentials-get=(), idle-detection=(), magnetometer=(), microphone=(), midi=(), payment=(), picture-in-picture=(), publickey-credentials-create=(), publickey-credentials-get=(), screen-wake-lock=(), storage-access=(), sync-xhr=(), usb=(), web-share=(), window-management=(), xr-spatial-tracking=()",
    "content-security-policy": [
      "base-uri 'self'",
      "connect-src 'self' http://localhost:5000",
      "default-src 'self'",
      "frame-ancestors 'none'",
      "manifest-src 'none'",
      "script-src 'self' 'unsafe-inline'",
      "style-src 'self' 'unsafe-inline'",
      "worker-src 'self'"
    ].join("; ")
  };
};

export default defineConfig(() => {
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
      oxlint: {
        lintCommand: "oxlint"
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
      host: "localhost",
      port: 3000,
      strictPort: true,
      headers: buildSecurityHeaders()
    },
    preview: {
      host: "localhost",
      port: 4173,
      strictPort: true
    }
  };
});
