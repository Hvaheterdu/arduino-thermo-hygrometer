import { createBrowserRouter, redirect, type DataRouter, type RouteObject } from "react-router";
import { About } from "./pages/about/About.page";
import { Home } from "./pages/home/Home.page";
import { Statistics } from "./pages/statistics/Statistics.page";

const basename = "/arduinothermohygrometer";

const routes: RouteObject[] = [
  {
    path: "/",
    children: [
      {
        index: true,
        loader: () => redirect("/home")
      },
      {
        path: "home",
        Component: Home
      },
      {
        path: "about",
        Component: About
      },
      {
        path: "statistics",
        Component: Statistics
      }
    ]
  }
];

export const router: DataRouter = createBrowserRouter(routes, { basename: basename });
