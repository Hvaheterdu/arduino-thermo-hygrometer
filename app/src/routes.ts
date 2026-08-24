import { createBrowserRouter, type DataRouter } from "react-router";

import { AppLayout, ErrorBoundary } from "@/components";
import { NotFoundPage } from "@/pages";

export const router: DataRouter = createBrowserRouter([
  {
    path: "/",
    Component: AppLayout,
    ErrorBoundary,
    children: [
      {
        index: true,
        lazy: async () => {
          const page = await import("@/pages/dashboard/Dashboard.page");
          const loader = await import("@/pages/dashboard/dashboard.loader");
          return { Component: page.DashboardPage, loader: loader.dashboardLoader };
        }
      },
      {
        path: "history",
        lazy: async () => {
          const page = await import("@/pages/history/History.page");
          const loader = await import("@/pages/history/history.loader");
          return { Component: page.HistoryPage, loader: loader.historyLoader };
        }
      },
      {
        path: "create",
        lazy: async () => {
          const page = await import("@/pages/create/Create.page");
          return { Component: page.CreatePage };
        }
      },
      { path: "*", Component: NotFoundPage }
    ]
  }
], {
  basename: import.meta.env.VITE_BASE_PATH
});
