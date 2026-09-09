import { screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";

import { Button } from "@/components/primitives/Button.component";
import { renderWithProviders } from "@/test/render.util";

describe("Button", () => {
  it("renders its label", () => {
    renderWithProviders(<Button>Save reading</Button>);

    expect(screen.getByRole("button", { name: "Save reading" })).toBeInTheDocument();
  });

  it("calls onClick when clicked", async () => {
    const handleClick = vi.fn();
    renderWithProviders(<Button onClick={handleClick}>Save reading</Button>);
    const button = screen.getByRole("button", { name: "Save reading" });

    await userEvent.click(button);

    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it("disables the button while loading", () => {
    renderWithProviders(<Button loading>Save reading</Button>);

    expect(screen.getByRole("button")).toBeDisabled();
  });
});
