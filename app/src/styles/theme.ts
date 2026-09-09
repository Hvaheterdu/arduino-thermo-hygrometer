import { createSystem, defaultConfig, defineConfig } from "@chakra-ui/react";

const config = defineConfig({
  theme: {
    tokens: {
      colors: {
        brand: {
          50: { value: "#e6fbf8" },
          100: { value: "#b3f2e9" },
          200: { value: "#80e9da" },
          300: { value: "#4de0cb" },
          400: { value: "#26d3ba" },
          500: { value: "#0fb8a0" },
          600: { value: "#0a8f7c" },
          700: { value: "#076658" },
          800: { value: "#043d35" },
          900: { value: "#021f1b" }
        }
      }
    },
    semanticTokens: {
      colors: {
        brand: {
          solid: { value: "{colors.brand.700}" },
          contrast: { value: "white" },
          fg: { value: "{colors.brand.700}" },
          muted: { value: "{colors.brand.100}" },
          subtle: { value: "{colors.brand.50}" },
          emphasized: { value: "{colors.brand.300}" },
          focusRing: { value: "{colors.brand.700}" }
        }
      }
    }
  }
});

export const theme = createSystem(defaultConfig, config);
