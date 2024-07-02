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
        "default-src 'self'; connect-src http: https: ws: 'self'; script-src 'self' 'unsafe-inline'; style-src 'self'; img-src 'self'; object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'self';",
      "Permisson-Policy":
        "accelerometer=(), ambient-light-sensor=(), autoplay=(), battery=(), camera=(), cross-origin-isolated=(), display-capture=(), document-domain=(), encrypted-media=(), execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(), geolocation=(), gyroscope=(), keyboard-map=(), magnetometer=(), microphone=(), midi=(), navigation-override=(), payment=(), picture-in-picture=(), publickey-credentials-get=(), screen-wake-lock=(), sync-xhr=(), usb=(), web-share=(), xr-spatial-tracking=())",
      "Referrer-Policy": "no-referrer",
      "X-Frame-Options": "SAMEORIGIN",
      "X-Content-Type-Options": "nosniff",
      "X-Permitted-Cross-Domain-Policies": "none",
    },
  },
  assetsInclude: ["**/*.svg"],
});
