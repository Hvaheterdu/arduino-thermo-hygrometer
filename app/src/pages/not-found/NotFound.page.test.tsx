import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen } from "@testing-library/react";
import { createMemoryRouter, RouterProvider } from "react-router";
import { describe, expect, it } from "vitest";

import { NotFoundPage } from "@/pages";

describe("NotFoundPage", () => {
  it("offers a path back to the dashboard", () => {
    const router = createMemoryRouter([{ path: "*", Component: NotFoundPage }], { initialEntries: ["/missing"] });

    render(
      <ChakraProvider value={defaultSystem}>
        <RouterProvider router={router} />
      </ChakraProvider>
    );

    expect(screen.getByRole("heading", { name: "Page not found" })).toBeDefined();
    expect(screen.getByRole("link", { name: "Back to dashboard" })).toHaveAttribute("href", "/");
  });
});
