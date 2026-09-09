import { createBrowserRouter, type DataRouter } from "react-router";

import { RootErrorBoundary } from "@/components/errorBoundary/RootErrorBoundary.component";
import { AppShell } from "@/components/layout/AppShell.component";
import { getBasePath } from "@/utils/env.util";
import { ROUTES } from "@/utils/routes.util";

export const router: DataRouter = createBrowserRouter(
  [
    {
      path: "/",
      Component: AppShell,
      ErrorBoundary: RootErrorBoundary,
      children: [
        {
          path: ROUTES.DASHBOARD,
          lazy: async () => {
            const { DashboardPage } = await import("@/pages/dashboard/Dashboard.page");
            return { Component: DashboardPage };
          }
        },
        {
          path: ROUTES.HISTORY,
          lazy: async () => {
            const { HistoryPage } = await import("@/pages/history/History.page");
            return { Component: HistoryPage };
          }
        },
        {
          path: "*",
          lazy: async () => {
            const { NotFoundPage } = await import("@/pages/notFound/NotFound.page");
            return { Component: NotFoundPage };
          }
        }
      ]
    }
  ],
  { basename: getBasePath() }
);
