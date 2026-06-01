// @vitest-environment jsdom

import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, it, vi } from "vitest";
import { useHistoricalReadings } from "../features/readings/hooks/useHistoricalReadings";
import type { FilterDraft } from "../features/readings/model/historyFilters";
import { initialFilterDraft } from "../features/readings/model/historyFilters";
import { DashboardPage } from "../pages/dashboard/DashboardPage";

vi.mock("../features/readings/hooks/useHistoricalReadings", () => {
  return {
    useHistoricalReadings: vi.fn()
  };
});

vi.mock("../features/readings/components/HistoryFiltersPanel", () => {
  return {
    HistoryFiltersPanel: ({
      draft,
      onDraftChange,
      onSubmit
    }: {
      draft: FilterDraft;
      onDraftChange: (nextDraft: FilterDraft) => void;
      onSubmit: () => void;
    }) => {
      return (
        <div>
          <button
            onClick={() => {
              onDraftChange({ ...draft, searchText: "attic" });
            }}
            type="button"
          >
            Change filters
          </button>
          <button onClick={onSubmit} type="button">
            Submit filters
          </button>
        </div>
      );
    }
  };
});

describe("DashboardPage", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("submits updated filters after draft changes", async () => {
    vi.mocked(useHistoricalReadings).mockReturnValue({
      data: {
        battery: [],
        temperature: [],
        humidity: []
      },
      isFetching: false,
      isError: false
    } as unknown as ReturnType<typeof useHistoricalReadings>);

    render(<DashboardPage />);

    fireEvent.click(screen.getByRole("button", { name: "Change filters" }));
    fireEvent.click(screen.getByRole("button", { name: "Submit filters" }));

    await waitFor(() => {
      const latestUseHistoricalReadingsCall = vi.mocked(useHistoricalReadings).mock.calls.at(-1);
      expect(latestUseHistoricalReadingsCall).toBeDefined();

      if (latestUseHistoricalReadingsCall === undefined) {
        throw new Error("Expected a useHistoricalReadings call after filter submission.");
      }

      const submittedFiltersArgument = latestUseHistoricalReadingsCall[0];
      expect(submittedFiltersArgument.searchFilters.searchText).toBe("attic");
    });
  });

  it("shows an alert when historical readings fail to load", () => {
    vi.mocked(useHistoricalReadings).mockReturnValue({
      data: undefined,
      isFetching: false,
      isError: true,
      error: new Error("Backend not reachable")
    } as ReturnType<typeof useHistoricalReadings>);

    render(<DashboardPage />);

    const alertElement = screen.getByRole("alert");
    expect(alertElement.textContent).toContain("Unable to load readings");
    expect(alertElement.textContent).toContain("Backend not reachable");
  });

  it("uses initial submitted filters before user interaction", () => {
    vi.mocked(useHistoricalReadings).mockReturnValue({
      data: {
        battery: [],
        temperature: [],
        humidity: []
      },
      isFetching: false,
      isError: false
    } as unknown as ReturnType<typeof useHistoricalReadings>);

    render(<DashboardPage />);

    const firstUseHistoricalReadingsCall = vi.mocked(useHistoricalReadings).mock.calls[0];
    expect(firstUseHistoricalReadingsCall).toBeDefined();

    if (firstUseHistoricalReadingsCall === undefined) {
      throw new Error("Expected an initial useHistoricalReadings call.");
    }

    const firstSubmittedFiltersArgument = firstUseHistoricalReadingsCall[0];
    expect(firstSubmittedFiltersArgument.searchFilters.searchText).toBe(initialFilterDraft.searchText);
  });
});
