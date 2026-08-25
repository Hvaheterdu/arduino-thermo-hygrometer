import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { render, screen } from "@testing-library/react";
import { RouterProvider, createMemoryRouter } from "react-router";
import { describe, expect, it } from "vitest";

import { NotFoundPage } from "./NotFound.page";

describe("NotFoundPage", () => {
  it("offers a path back to the dashboard", () => {
    const router = createMemoryRouter([{ Component: NotFoundPage, path: "*" }], { initialEntries: ["/missing"] });

    render(
      <ChakraProvider value={defaultSystem}>
        <RouterProvider router={router} />
      </ChakraProvider>
    );

    expect(screen.getByRole("heading", { name: "Page not found" })).toBeInTheDocument();
    expect(screen.getByRole("link", { name: "Back to dashboard" })).toHaveAttribute("href", "/");
  });
});
