import { screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { Input } from "@/components/primitives/Input.component";
import { renderWithProviders } from "@/test/render.util";

describe("Input", () => {
  it("associates the visible label with the input for screen readers", () => {
    renderWithProviders(<Input label="Battery status (%)" />);

    expect(screen.getByLabelText("Battery status (%)")).toBeInTheDocument();
  });

  it("marks the field invalid when an error message is present", () => {
    renderWithProviders(<Input label="Battery status (%)" errorText="Must not be null." />);

    expect(screen.getByLabelText("Battery status (%)")).toHaveAttribute("aria-invalid", "true");
  });

  it("shows the error message instead of the helper text", () => {
    renderWithProviders(<Input label="Battery status (%)" helperText="0-100" errorText="Must not be null." />);

    expect(screen.getByText("Must not be null.")).toBeInTheDocument();
    expect(screen.queryByText("0-100")).not.toBeInTheDocument();
  });
});
