import { StrictMode, type ReactElement } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router";

import { AppProvider } from "@/AppProvider";
import { router } from "@/routes";

import "@/styles/globals.css";

const rootElement = document.getElementById("root");
if (!rootElement) {
  throw new Error("Root element with id 'root' was not found in index.html.");
}

const App = (): ReactElement => (
  <StrictMode>
    <AppProvider>
      <RouterProvider router={router} />
    </AppProvider>
  </StrictMode>
);

createRoot(rootElement).render(<App />);
