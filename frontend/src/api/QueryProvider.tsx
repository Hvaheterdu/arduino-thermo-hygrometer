import { QueryCache, QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { memo } from "react";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    onError: (error: any) => {
      if (error.status === 401) {
        const currentUrl = window.location.href;
        window.location.assign(
          `/api/auth/login?postLoginRedirectUri=${encodeURIComponent(currentUrl)}`
        );
      }
    },
  }),
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 5,
      refetchOnWindowFocus: false,
      retry: false,
      retryDelay: 1000 * 60 * 5,
      retryOnMount: false,
    },
  },
});

interface ReactQueryProviderProps {
  children: React.ReactNode;
}

const ReactQueryProviderComponent = ({ children }: ReactQueryProviderProps) => {
  return (
    <QueryClientProvider client={queryClient}>
      {children}
      <ReactQueryDevtools />
    </QueryClientProvider>
  );
};

export const ReactQueryProvider = memo(ReactQueryProviderComponent);
