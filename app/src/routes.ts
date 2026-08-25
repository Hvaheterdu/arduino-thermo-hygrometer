import { createBrowserRouter, type DataRouter } from "react-router";

import { ErrorBoundary } from "@/components/error-boundary/ErrorBoundary.component";
import { AppLayout } from "@/components/layout/AppLayout.component";
import { NotFoundPage } from "@/pages/not-found/NotFound.page";

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
            const { DashboardPage } = await import("@/pages/dashboard/Dashboard.page");

            return {
              Component: DashboardPage,
              loader: loader.dashboardLoader
            };
          }
        },
        {
          lazy: async () => {
            const loader = await import("./pages/history/history.loader");
            const { HistoryPage } = await import("@/pages/history/History.page");

            return {
              Component: HistoryPage,
              loader: loader.historyLoader
            };
          },
          path: "history"
        },
        {
          lazy: async () => {
            const { CreatePage } = await import("@/pages/create/Create.page");
            return { Component: CreatePage };
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
