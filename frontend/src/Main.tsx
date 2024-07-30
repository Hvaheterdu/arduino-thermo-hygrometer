/* eslint @typescript-eslint/naming-convention: 0 */
import { StrictMode } from "react";
import ReactDOM from "react-dom/client";
import { App } from "./App";

const rootElement: HTMLElement = document.getElementById("root") ?? document.createElement("div");

const root = ReactDOM.createRoot(rootElement);
root.render(
  <StrictMode>
    <App />
  </StrictMode>
);
