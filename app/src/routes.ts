import { createElement } from "react";
import { Navigate, createBrowserRouter, type DataRouter, type RouteObject } from "react-router";
import { AppLayout } from "./app/AppLayout";
import { DashboardPage } from "./pages/dashboard/DashboardPage";
import { SensorHealthPage } from "./pages/sensorhealth/SensorHealthPage";
import { TrendsPage } from "./pages/trends/TrendsPage";

const basename = "/arduinothermohygrometer";

const routes: RouteObject[] = [
  {
    path: "/",
    element: createElement(AppLayout),
    children: [
      {
        index: true,
        element: createElement(DashboardPage)
      },
      {
        path: "trends",
        element: createElement(TrendsPage)
      },
      {
        path: "sensor-health",
        element: createElement(SensorHealthPage)
      }
    ]
  },
  {
    path: "*",
    element: createElement(Navigate, { replace: true, to: "/" })
  }
];

export const router: DataRouter = createBrowserRouter(routes, { basename: basename });
