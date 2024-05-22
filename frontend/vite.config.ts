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
    https: true,
    cors: true,
    headers: {},
  },
  assetsInclude: ["**/*.svg"],
});
