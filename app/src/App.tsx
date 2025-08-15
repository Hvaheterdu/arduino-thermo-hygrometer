import type { JSX } from "react";
import { RouterProvider } from "react-router";
import { router } from "./routes";

export const App = (): JSX.Element => {
  return <RouterProvider router={router} />;
};
