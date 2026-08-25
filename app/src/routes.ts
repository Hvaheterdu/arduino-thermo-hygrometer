import { createBrowserRouter, type DataRouter } from "react-router";

import { AppLayout, ErrorBoundary } from "./components";
import { NotFoundPage } from "./pages";

export const router: DataRouter = createBrowserRouter(
  [
    {
      Component: AppLayout,
      ErrorBoundary,
      children: [
        {
          index: true,
          lazy: async () => {
            const loader = await import("./pages/dashboard/dashboard.loader");
            const page = await import("./pages/dashboard/Dashboard.page");

            return {
              Component: page.DashboardPage,
              loader: loader.dashboardLoader
            };
          }
        },
        {
          lazy: async () => {
            const loader = await import("./pages/history/history.loader");
            const page = await import("./pages/history/History.page");

            return {
              Component: page.HistoryPage,
              loader: loader.historyLoader
            };
          },
          path: "history"
        },
        {
          lazy: async () => {
            const page = await import("./pages/create/Create.page");
            return { Component: page.CreatePage };
          },
          path: "create"
        },
        {
          Component: NotFoundPage,
          path: "*"
        }
      ],
      path: "/"
    }
  ],
  {
    basename: import.meta.env.VITE_BASE_PATH
  }
);
