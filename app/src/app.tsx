import type { JSX } from "react";
import { RouterProvider } from "react-router";
import { AppProviders } from "./app/AppProviders";
import { router } from "./routes";

export const App = (): JSX.Element => {
  return (
    <AppProviders>
      <RouterProvider router={router} />
    </AppProviders>
  );
};
