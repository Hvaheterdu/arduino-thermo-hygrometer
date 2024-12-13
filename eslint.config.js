import js from "@eslint/js";
import checkFile from "eslint-plugin-check-file";
import eslintPluginImportX from "eslint-plugin-import-x";
import jsdoc from "eslint-plugin-jsdoc";
import eslintPluginPrettier from "eslint-plugin-prettier/recommended";
import reactPlugin from "eslint-plugin-react";
import eslintPluginHooks from "eslint-plugin-react-hooks";
import reactRefresh from "eslint-plugin-react-refresh";
import globals from "globals";
import tseslint from "typescript-eslint";

export default [
  js.configs.recommended,
  ...tseslint.configs.recommendedTypeChecked,
  ...tseslint.configs.stylisticTypeChecked,
  ...tseslint.configs.strictTypeChecked,
  eslintPluginImportX.flatConfigs.errors,
  eslintPluginImportX.flatConfigs.recommended,
  eslintPluginImportX.flatConfigs.typescript,
  eslintPluginImportX.flatConfigs.warnings,
  jsdoc.configs["flat/recommended-typescript-error"],
  reactPlugin.configs.flat["jsx-runtime"],
  reactPlugin.configs.flat.recommended,
  eslintPluginPrettier,
  {
    languageOptions: {
      parser: tseslint.parser,
      globals: {
        ...globals.browser
      },
      parserOptions: {
        ecmaVersion: "latest",
        sourceType: "module",
        projectService: "./tsconfig.json",
        project: true,
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
    files: ["**/*.ts", "**/*.tsx"],
    settings: {
      react: {
        version: "18.3"
      }
    },
    plugins: {
      "check-file": checkFile,
      "react-hooks": eslintPluginHooks,
      "react-refresh": reactRefresh,
      "@typescript-eslint": tseslint.plugin,
      react: reactPlugin
    },
    rules: {
      ...eslintPluginHooks.configs.recommended.rules,
      "check-file/filename-naming-convention": [
        "error",
        {
          "./app/**/*.{tsx}": "PASCAL_CASE",
          "./app/**/*.{ts}": "CAMEL_CASE",
          "./app/**/*.{d.ts}": "KEBAB_CASE"
        }
      ],
      "check-file/folder-naming-convention": [
        "error",
        {
          "./app/src/**/": "FLAT_CASE"
        }
      ],
      "no-console": "error",
      "no-debugger": "error",
      "no-unused-vars": "off",
      "no-use-before-define": "off",
      "import-x/no-default-export": "error",
      "import-x/no-unresolved": "error",
      "import-x/no-rename-default": "error",
      "react-refresh/only-export-components": "warn",
      "react/react-in-jsx-scope": "off",
      "@typescript-eslint/no-unsafe-argument": "error",
      "@typescript-eslint/no-unsafe-assignment": "error",
      "@typescript-eslint/no-unsafe-call": "error",
      "@typescript-eslint/no-unsafe-member-access": "error",
      "@typescript-eslint/no-unsafe-return": "error",
      "@typescript-eslint/no-namespace": "off",
      "@typescript-eslint/prefer-namespace-keyword": "off",
      "@typescript-eslint/no-unused-vars": [
        "error",
        {
          ignoreRestSiblings: true
        }
      ],
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
          selector: "enum",
          format: ["PascalCase"]
        },
        {
          selector: "variable",
          format: ["camelCase", "PascalCase"]
        }
      ]
    }
  },
  {
    ignores: [
      "api",
      "api/**/bin",
      "build",
      "coverage",
      "dist",
      "node_modules",
      "**/*.js",
      "**/*.generated.ts",
      "**/.git",
      "**/.svn",
      "**/.hg"
    ]
  }
];
