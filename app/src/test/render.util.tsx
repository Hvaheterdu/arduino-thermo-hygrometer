import { ChakraProvider } from "@chakra-ui/react";
import { render, type RenderResult } from "@testing-library/react";
import type { ReactElement } from "react";
import { MemoryRouter } from "react-router";
import { SWRConfig } from "swr";

import { theme } from "@/styles/theme";

export const renderWithProviders = (children: ReactElement, options?: { route?: string }): RenderResult => {
  const { route = "/" } = options ?? {};

  return render(
    <ChakraProvider value={theme}>
      <SWRConfig value={{ provider: () => new Map(), dedupingInterval: 0 }}>
        <MemoryRouter initialEntries={[route]}>{children}</MemoryRouter>
      </SWRConfig>
    </ChakraProvider>
  );
};
