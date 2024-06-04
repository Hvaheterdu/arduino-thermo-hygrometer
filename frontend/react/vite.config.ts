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
      "Content-Security-Policy":
        "default-src 'self'; connect-src ws: http: https: 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:;",
      "Permisson-Policy":
        "accelerometer 'none'; camera 'none'; geolocation 'none'; gyroscope 'none'; magnetometer 'none'; microphone 'none'; payment 'none'; usb 'none'",
      "Referrer-Policy": "no-referrer",
      "X-Frame-Options": "SAMEORIGIN",
      "X-Content-Type-Options": "nosniff",
      "X-Permitted-Cross-Domain-Policies": "none",
    },
  },
  assetsInclude: ["**/*.svg"],
});
