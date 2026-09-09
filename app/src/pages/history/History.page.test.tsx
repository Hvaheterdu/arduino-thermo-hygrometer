import { screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { Route, Routes } from "react-router";
import { beforeEach, describe, expect, it, vi } from "vitest";

import { useDeleteMeasurement } from "@/hooks/useDeleteMeasurement";
import { useMeasurements } from "@/hooks/useMeasurements";
import { HistoryPage } from "@/pages/history/History.page";
import { renderWithProviders } from "@/test/render.util";

vi.mock("@/hooks/useMeasurements");
vi.mock("@/hooks/useDeleteMeasurement");

const renderHistory = (resource: string): void => {
  renderWithProviders(
    <Routes>
      <Route path="/history/:resource" element={<HistoryPage />} />
    </Routes>,
    { route: `/history/${resource}` }
  );
};

describe("HistoryPage", () => {
  it("renders the not-found page for an unknown resource", () => {
    renderHistory("unknown");

    expect(screen.getByRole("heading", { name: "Page not found" })).toBeInTheDocument();
  });

  describe("with a known resource", () => {
    beforeEach(() => {
      vi.spyOn({ useMeasurements }, "useMeasurements").mockReturnValue({
        data: [{ registeredAt: "2017-07-21T17:00:00Z", temp: 21.4 } as never],
        error: undefined,
        isLoading: false
      } as never);
      vi.spyOn({ useDeleteMeasurement }, "useDeleteMeasurement").mockReturnValue({
        trigger: vi.fn(),
        isMutating: false,
        error: undefined
      } as never);

      renderHistory("temperature");
    });

    it("renders the resource heading", () => {
      expect(screen.getByRole("heading", { name: "Temperature history" })).toBeInTheDocument();
    });

    it("renders the fetched readings", () => {
      expect(screen.getByText("21.4°C")).toBeInTheDocument();
    });

    it("accepts an exact local date and time when date-only matching is disabled", async () => {
      const user = userEvent.setup();

      await user.click(screen.getByRole("checkbox", { name: "Match by date only" }));

      expect(screen.getByLabelText("Date and time")).toHaveAttribute("type", "datetime-local");
    });
  });
});
