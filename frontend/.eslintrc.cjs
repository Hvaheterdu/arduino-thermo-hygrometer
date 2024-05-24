module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  extends: [
    "eslint:recommended",
    "plugin:import/errors",
    "plugin:import/warnings",
    "plugin:import/typescript",
    "plugin:@typescript-eslint/recommended-type-checked",
    "plugin:@typescript-eslint/stylistic-type-checked",
    "plugin:react/recommended",
    "plugin:react/jsx-runtime",
    "plugin:react-hooks/recommended",
    "prettier",
  ],
  ignorePatterns: ["dist", ".eslintrc.cjs", "vite.config.ts"],
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaVersion: "latest",
    sourceType: "module",
    project: "./tsconfig.json",
    tsconfigRootDir: __dirname,
    ecmaFeatures: {
      jsx: true,
    },
    settings: {
      react: {
        version: "18.2",
      },
    },
    plugins: ["@typescript-eslint", "react", "react-hooks", "react-refresh", "prettier"],
    rules: {
      "no-console": "error",
      "react/prop-types": [
        "warn",
        {
          skipUndeclared: true,
        },
      ],
      "import/no-unresolved": "error",
      "no-unused-vars": "off",
      "no-debugger": "error",
      "react/jsx-uses-react": "off",
      "react/react-in-jsx-scope": "off",
      "react-refresh/only-export-components": "warn",
      "@typescript-eslint/no-namespace": "off",
      "@typescript-eslint/prefer-namespace-keyword": "off",
      "@typescript-eslint/no-unused-vars": [
        "error",
        {
          ignoreRestSiblings: true,
        },
      ],
    },
  },
};
