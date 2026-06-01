import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import type { JSX, PropsWithChildren } from "react";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false
    }
  }
});

export const AppProviders = ({ children }: PropsWithChildren): JSX.Element => {
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>;
};
