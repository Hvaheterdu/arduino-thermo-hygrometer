import js from "@eslint/js";
import vitest from "@vitest/eslint-plugin";
import eslintPluginCheckFile from "eslint-plugin-check-file";
import eslintPluginPrettier from "eslint-plugin-prettier/recommended";
import reactPlugin from "eslint-plugin-react";
import eslintPluginHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";
import eslintPluginTestingLibrary from "eslint-plugin-testing-library";
import globals from "globals";
import tseslint from "typescript-eslint";

export default [
  js.configs.recommended,
  ...tseslint.configs.strictTypeChecked,
  ...tseslint.configs.stylisticTypeChecked,
  eslintPluginTestingLibrary.configs["flat/react"],
  reactPlugin.configs.flat["jsx-runtime"],
  reactPlugin.configs.flat.recommended,
  {
    languageOptions: {
      parser: tseslint.parser,
      globals: {
        ...globals.browser,
        ...globals.node,
        ...vitest.environments.env.globals
      },
      parserOptions: {
        ecmaVersion: "latest",
        sourceType: "module",
        projectService: true,
        tsconfigRootDir: import.meta.dirname,
        ecmaFeatures: {
          jsx: true,
          impliedStrict: true
        }
      }
    },
    linterOptions: {
      reportUnusedDisableDirectives: "error"
    },
    settings: {
      react: {
        version: "detect"
      }
    },
    plugins: {
      "check-file": eslintPluginCheckFile,
      "react-hooks": eslintPluginHooks,
      "react-refresh": reactRefresh,
      "testing-library": eslintPluginTestingLibrary,
      "@typescript-eslint": tseslint.plugin,
      react: reactPlugin
    },
    rules: {
      ...eslintPluginHooks.configs.recommended.rules,
      "check-file/filename-naming-convention": [
        "error",
        {
          "app/**/*.{tsx}": "PASCAL_CASE",
          "app/**/*.{ts}": "CAMEL_CASE",
          "app/**/*.{d.ts}": "KEBAB_CASE"
        }
      ],
      "check-file/folder-naming-convention": [
        "error",
        {
          "app/src/**/": "FLAT_CASE"
        }
      ],
      "no-console": "error",
      "react/react-in-jsx-scope": "off",
      "react-refresh/only-export-components": "warn",
      "@typescript-eslint/no-use-before-define": "error",
      "@typescript-eslint/naming-convention": [
        "error",
        {
          selector: "enum",
          format: ["PascalCase"]
        },
        {
          selector: "enumMember",
          format: ["PascalCase"]
        },
        {
          selector: "function",
          format: ["camelCase", "PascalCase"]
        },
        {
          selector: "interface",
          format: ["PascalCase"]
        },
        {
          selector: "import",
          format: ["camelCase", "PascalCase"]
        },
        {
          selector: "typeProperty",
          format: ["snake_case", "UPPER_CASE"]
        },
        {
          selector: "variable",
          format: ["camelCase", "PascalCase"]
        }
      ]
    }
  },
  {
    files: ["**/*.{test,spec}.{ts,tsx}", "**/__tests__/**/*.{ts,tsx}", "**/__mocks__/**/*.{ts}"],
    plugins: {
      vitest
    },
    rules: {
      ...vitest.configs.recommended.rules,
      "vitest/max-nested-describe": ["error", { max: 1 }]
    },
    settings: {
      vitest: {
        typecheck: true
      }
    }
  },
  eslintPluginPrettier,
  {
    ignores: [
      "api",
      "build",
      "coverage",
      "dist",
      "node_modules",
      "scripts",
      "eslint.config.js",
      "tsconfig.json",
      "tsconfig.app.json",
      "tsconfig.node.json",
      "app/vite.config.ts",
      "**/*.d.ts",
      "**/*.generated.ts",
      "**/.git",
      "**/.svn",
      "**/.hg"
    ]
  }
];
