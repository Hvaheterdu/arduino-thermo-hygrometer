import { createBrowserRouter, type DataRouter, type RouteObject } from "react-router";

const basename = "/arduinothermohygrometer";

const routes: RouteObject[] = [
  {
    path: "/",
    children: []
  }
];

export const router: DataRouter = createBrowserRouter(routes, { basename: basename });
