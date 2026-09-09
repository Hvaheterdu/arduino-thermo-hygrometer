import { screen } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useMeasurements } from "@/hooks/useMeasurements";
import { DashboardPage } from "@/pages/dashboard/Dashboard.page";
import { renderWithProviders } from "@/test/render.util";

vi.mock("@/hooks/useMeasurements");

const renderDashboard = (): void => {
  vi.spyOn({ useMeasurements }, "useMeasurements").mockReturnValue({
    data: [],
    error: undefined,
    isLoading: false
  } as never);
  renderWithProviders(<DashboardPage />);
};

describe("DashboardPage", () => {
  beforeEach(renderDashboard);

  it("renders the page heading", () => {
    expect(screen.getByRole("heading", { name: "Today's readings" })).toBeInTheDocument();
  });

  it.each(["battery", "humidity", "temperature"])("renders an add-%s-reading button", (resource) => {
    expect(screen.getByRole("button", { name: `Add ${resource} reading` })).toBeInTheDocument();
  });
});
