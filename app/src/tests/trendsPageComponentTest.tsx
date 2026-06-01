// @vitest-environment jsdom

import { fireEvent, render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { useTrendsInsights } from "../features/insights/hooks/useTrendsInsights";
import type { TrendsInsights } from "../features/insights/model/insightTypes";
import { TrendsPage } from "../pages/trends/TrendsPage";
import { LocationSearchDebug } from "./LocationSearchDebug";

vi.mock("../features/insights/hooks/useTrendsInsights", () => {
  return {
    useTrendsInsights: vi.fn()
  };
});

const trendsInsightsFixture: TrendsInsights = {
  points: [],
  battery: { label: "Battery", currentValue: 92, deltaValue: -3, direction: "down" },
  temperature: { label: "Temperature", currentValue: 21, deltaValue: 0.5, direction: "up" },
  humidity: { label: "Humidity", currentValue: 42, deltaValue: 1.2, direction: "up" },
  failedDays: 0
};

const renderTrendsPage = (): void => {
  render(
    <MemoryRouter initialEntries={["/trends"]}>
      <Routes>
        <Route
          path="/trends"
          element={
            <>
              <TrendsPage />
              <LocationSearchDebug />
            </>
          }
        />
      </Routes>
    </MemoryRouter>
  );
};

describe("TrendsPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("updates search params when metric visibility toggle is changed", () => {
    vi.mocked(useTrendsInsights).mockReturnValue({
      data: trendsInsightsFixture,
      isError: false,
      isFetching: false
    } as ReturnType<typeof useTrendsInsights>);

    renderTrendsPage();

    expect(screen.getByTestId("location-search").textContent).toBe("");
    fireEvent.click(screen.getByRole("button", { name: "Battery" }));
    expect(screen.getByTestId("location-search").textContent).toBe("?b=0");

    fireEvent.click(screen.getByRole("button", { name: "Battery" }));
    expect(screen.getByTestId("location-search").textContent).toBe("");
  });

  it("shows an alert when trends loading fails", () => {
    vi.mocked(useTrendsInsights).mockReturnValue({
      data: undefined,
      isError: true,
      error: new Error("Failed to load trends"),
      isFetching: false
    } as ReturnType<typeof useTrendsInsights>);

    renderTrendsPage();

    const alertElement = screen.getByRole("alert");
    expect(alertElement.textContent).toContain("Unable to load trends");
    expect(alertElement.textContent).toContain("Failed to load trends");
  });
});
