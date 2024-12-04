/* eslint import-x/no-default-export: 0 */
/* eslint @typescript-eslint/naming-convention: 0 */

import viteReact from "@vitejs/plugin-react";
import { defineConfig } from "vite";
import { checker } from "vite-plugin-checker";
import plugin_default from "vite-plugin-mkcert";
import vitePluginSvgr from "vite-plugin-svgr";

// https://vitejs.dev/config/
export default defineConfig({
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
        lintCommand: 'eslint "**/*.{ts,tsx}"'
      }
    }),
    plugin_default(),
    viteReact(),
    vitePluginSvgr()
  ],
  build: {
    outDir: "./dist",
    sourcemap: true,
    reportCompressedSize: true
  },
  server: {
    host: "localhost",
    port: 3001,
    strictPort: true,
    cors: {
      origin: "https://localhost:5001"
    },
    headers: {
      "content-security-policy":
        "base-uri 'self'; default-src 'self'; frame-ancestors 'none'; manifest-src 'none'; script-src 'unsafe-inline' 'self'; style-src 'unsafe-inline' 'self'",
      "permissions-policy":
        "accelerometer=(), autoplay=(), bluetooth=(), camera=(), compute-pressure=(), cross-origin-isolated=(), display-capture=(), encrypted-media=(), fullscreen=(), geolocation=(), gyroscope=(), hid=(), identity-credentials-get=(), idle-detection=(), magnetometer=(), microphone=(), midi=(), payment=(), picture-in-picture=(), publickey-credentials-create=(), publickey-credentials-get=(), screen-wake-lock=(), storage-access=(), sync-xhr=(), usb=(), web-share=(), window-management=(), xr-spatial-tracking=()",
      "referrer-policy": "no-referrer",
      "strict-transport-security": "max-age=31536000; includeSubDomains; preload",
      "x-content-type-options": "nosniff"
    }
  },
  assetsInclude: ["**/*.svg"]
});
