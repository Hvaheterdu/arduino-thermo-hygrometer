import { createBrowserRouter, type DataRouter } from "react-router";

import { App } from "@/App";

export const router: DataRouter = createBrowserRouter(
  [
    {
      path: "/",
      Component: App,
      children: []
    }
  ],
  {
    basename: import.meta.env.VITE_BASE_PATH
  }
);
