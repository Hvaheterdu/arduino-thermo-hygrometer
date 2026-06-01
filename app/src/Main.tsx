import { StrictMode } from "react";
import { createRoot, type Root } from "react-dom/client";
import { App } from "./App";
import "./styles/global.css";

const rootElement: HTMLElement = document.getElementById("root") ?? document.createElement("div");

const root: Root = createRoot(rootElement);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
