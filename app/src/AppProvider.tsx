import { ChakraProvider } from "@chakra-ui/react";
import type { ReactElement, ReactNode } from "react";
import { SWRConfig } from "swr";

import { theme } from "@/styles/theme";

type AppProviderProps = {
  children: ReactNode;
};

export const AppProvider = ({ children }: AppProviderProps): ReactElement => {
  return (
    <ChakraProvider value={theme}>
      <SWRConfig value={{ revalidateOnFocus: false, shouldRetryOnError: false }}>{children}</SWRConfig>
    </ChakraProvider>
  );
};
