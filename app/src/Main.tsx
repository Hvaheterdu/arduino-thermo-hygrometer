import { StrictMode } from "react";
import { createRoot, type Root } from "react-dom/client";
import { App } from "./App";

const rootElement: HTMLElement = document.getElementById("root") ?? document.createElement("div");

const root: Root = createRoot(rootElement);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
