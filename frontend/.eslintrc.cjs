module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  plugins: [
    "@typescript-eslint",
    "check-file",
    "react",
    "react-hooks",
    "react-refresh",
    "prettier",
  ],
  extends: [
    "eslint:recommended",
    "plugin:react/recommended",
    "plugin:react/jsx-runtime",
    "plugin:react-hooks/recommended",
    "plugin:@typescript-eslint/recommended-type-checked",
    "plugin:@typescript-eslint/strict-type-checked",
    "plugin:@typescript-eslint/stylistic-type-checked",
    "plugin:import-x/recommended",
    "plugin:import-x/errors",
    "plugin:import-x/warnings",
    "plugin:import-x/typescript",
    "prettier",
  ],
  ignorePatterns: [
    "build",
    "coverage",
    "dist",
    "node_modules",
    "*.cjs",
    "**/*.generated.ts",
    "**/.git",
    "**/.svn",
    "**/.hg",
  ],
  settings: {
    react: {
      version: "18.3",
    },
  },
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaVersion: "latest",
    sourceType: "module",
    project: "./tsconfig.app.json",
    tsconfigRootDir: __dirname,
    ecmaFeatures: {
      jsx: true,
      impliedStrict: true,
    },
  },
  rules: {
    "check-file/filename-naming-convention": [
      "error",
      {
        "**/*.{tsx}": "PASCAL_CASE",
        "**/*.{ts}": "KEBAB_CASE",
        "**/*.{d.ts}": "KEBAB_CASE",
      },
    ],
    "check-file/folder-naming-convention": [
      "error",
      {
        "src/**/": "FLAT_CASE",
      },
    ],
    "no-console": "error",
    "no-unused-vars": "off",
    "no-debugger": "error",
    "no-use-before-define": "error",
    "import-x/no-unresolved": "error",
    "import-x/no-default-export": "error",
    "react-refresh/only-export-components": "warn",
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
        ignoreRestSiblings: true,
      },
    ],
    "@typescript-eslint/naming-convention": [
      "error",
      {
        selector: "enum",
        format: ["PascalCase"],
      },
      {
        selector: "enumMember",
        format: ["PascalCase"],
      },
      {
        selector: "function",
        format: ["camelCase", "PascalCase"],
      },
      {
        selector: "interface",
        format: ["PascalCase"],
      },
      {
        selector: "typeProperty",
        format: ["snake_case", "UPPER_CASE"],
      },
      {
        selector: "enum",
        format: ["PascalCase"],
      },
      {
        selector: "variable",
        format: ["camelCase", "PascalCase"],
      },
    ],
  },
};
