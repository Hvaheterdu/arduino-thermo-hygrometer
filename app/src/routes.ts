import { createBrowserRouter, redirect, type DataRouter, type RouteObject } from "react-router";
import { About } from "./pages/about/About.page";
import { Home } from "./pages/home/Home.page";

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
