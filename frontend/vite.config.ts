/* eslint import-x/no-default-export: 0 */
import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";
import mkcert from "vite-plugin-mkcert";
import svgrPlugin from "vite-plugin-svgr";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [mkcert(), react(), svgrPlugin()],
  build: {
    outDir: "build",
    sourcemap: true,
    minify: true,
  },
  server: {
    host: "localhost",
    port: 3001,
    strictPort: true,
    cors: true,
    headers: {
      "referrer-policy": "no-referrer",
      "strict-transport-security": "max-age=31536000; includeSubDomains; preload",
      "x-frame-options": "SAMEORIGIN",
      "x-content-type-options": "nosniff",
    },
  },
  assetsInclude: ["**/*.svg"],
});
