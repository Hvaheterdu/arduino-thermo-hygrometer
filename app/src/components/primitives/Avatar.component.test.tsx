import { screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { Avatar } from "@/components/primitives/Avatar.component";
import { renderWithProviders } from "@/test/render.util";

describe("Avatar", () => {
  it("labels the device avatar with the current battery percentage", () => {
    renderWithProviders(<Avatar batteryStatus={72} />);

    expect(screen.getByLabelText("Arduino device, battery at 72%")).toBeInTheDocument();
  });
});
