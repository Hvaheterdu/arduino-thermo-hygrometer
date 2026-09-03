import { ChakraProvider, defaultSystem } from "@chakra-ui/react";
import { NotFoundPage } from "@pages/not-found/NotFound.page";
import { render, screen } from "@testing-library/react";
import { createMemoryRouter, RouterProvider } from "react-router";
import { describe, expect, it } from "vitest";

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
