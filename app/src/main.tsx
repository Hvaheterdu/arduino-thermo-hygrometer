import { type ReactElement, StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { RouterProvider } from "react-router";

import { router } from "@/router";

const rootElement = document.getElementById("root");
if (!rootElement) {
  throw new Error("Root element with id 'root' was not found in index.html.");
}

const App = (): ReactElement => (
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);

createRoot(rootElement).render(<App />);
