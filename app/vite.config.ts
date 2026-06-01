import viteReact from "@vitejs/plugin-react";
import fs from "node:fs";
import path from "node:path";
import { defineConfig } from "vite";
import { checker } from "vite-plugin-checker";
import plugin from "vite-plugin-mkcert";
import vitePluginSvgr from "vite-plugin-svgr";

export default defineConfig({
  root: import.meta.dirname,
  plugins: [
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
    plugin({
      savePath: "../.cert"
    }),
    viteReact(),
    vitePluginSvgr({
      include: "**/*.svg"
    })
  ],
  build: {
    outDir: "dist",
    sourcemap: true,
    reportCompressedSize: true
  },
  cacheDir: "node_modules/.vite/app",
  server: {
    https: {
      key: fs.readFileSync(path.resolve(import.meta.dirname, "../.cert/dev.pem")),
      cert: fs.readFileSync(path.resolve(import.meta.dirname, "../.cert/cert.pem"))
    },
    host: "localhost",
    port: 3000,
    strictPort: true,
    cors: {
      origin: "http://localhost:5000"
    },
    headers: {
      "content-security-policy":
        "base-uri 'self'; connect-src 'self' https://localhost:5000; default-src 'self'; frame-ancestors 'none'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; worker-src 'self'",
      "permissions-policy":
        "accelerometer=(), autoplay=(), bluetooth=(), camera=(), compute-pressure=(), cross-origin-isolated=(), display-capture=(), encrypted-media=(), fullscreen=(), geolocation=(), gyroscope=(), hid=(), identity-credentials-get=(), idle-detection=(), magnetometer=(), microphone=(), midi=(), payment=(), picture-in-picture=(), publickey-credentials-create=(), publickey-credentials-get=(), screen-wake-lock=(), storage-access=(), sync-xhr=(), usb=(), web-share=(), window-management=(), xr-spatial-tracking=()",
      "referrer-policy": "no-referrer",
      "strict-transport-security": "max-age=31536000; includeSubDomains; preload",
      "x-content-type-options": "nosniff"
    }
  }
});
