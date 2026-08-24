import type { PropsWithChildren, ReactElement } from "react";
import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { SWRConfig } from "swr";

export const AppProvider = ({ children }: PropsWithChildren): ReactElement => (
  <ChakraProvider value={defaultSystem}>
    <SWRConfig
      value={{
        revalidateOnFocus: false,
        shouldRetryOnError: false,
        keepPreviousData: true
      }}
    >
      {children}
    </SWRConfig>
  </ChakraProvider>
);
