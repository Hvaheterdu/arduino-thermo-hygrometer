import { screen } from "@testing-library/react";
import { beforeEach, describe, expect, it } from "vitest";

import { NotFoundPage } from "@/pages/notFound/NotFound.page";
import { renderWithProviders } from "@/test/render.util";

describe("NotFoundPage", () => {
  beforeEach(() => {
    renderWithProviders(<NotFoundPage />);
  });

  it("renders a heading", () => {
    expect(screen.getByRole("heading", { name: "Page not found" })).toBeInTheDocument();
  });

  it("renders a link back to the dashboard", () => {
    expect(screen.getByRole("link", { name: "Back to dashboard" })).toHaveAttribute("href", "/");
  });
});
