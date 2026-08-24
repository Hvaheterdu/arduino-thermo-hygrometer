import type { ReactElement } from "react";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router/dom";

import { AppProvider } from "@/AppProvider";
import { router } from "@/routes";

const root: HTMLElement | null = document.getElementById("root");

if (!root) {
  throw new Error("Application root was not found.");
}

const application: ReactElement = (
  <StrictMode>
    <AppProvider>
      <RouterProvider router={router} />
    </AppProvider>
  </StrictMode>
);

createRoot(root).render(application);
