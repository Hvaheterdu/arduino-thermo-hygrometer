import { createBrowserRouter, redirect, type DataRouter, type RouteObject } from "react-router";
import { About } from "./pages/About.page";
import { Home } from "./pages/Home.page";

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
      }
    ]
  }
];

export const router: DataRouter = createBrowserRouter(routes, { basename: basename });
