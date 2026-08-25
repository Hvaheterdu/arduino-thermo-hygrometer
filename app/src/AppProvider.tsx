import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import type { PropsWithChildren, ReactElement } from "react";

export const AppProvider = ({ children }: PropsWithChildren): ReactElement => (
  <ChakraProvider value={defaultSystem}>{children}</ChakraProvider>
);
