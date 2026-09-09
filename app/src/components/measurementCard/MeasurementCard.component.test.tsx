import { faBatteryFull, faTemperatureHalf } from "@fortawesome/free-solid-svg-icons";
import { screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";

import { MeasurementCard } from "@/components/measurementCard/MeasurementCard.component";
import { useMeasurements } from "@/hooks/useMeasurements";
import { renderWithProviders } from "@/test/render.util";
import { RESOURCE_CONFIG } from "@/utils/resourceConfig.util";

vi.mock("@/hooks/useMeasurements");

const query = { registeredAt: "2017-07-21", dateOnly: true };

const mockUseMeasurements = (data: unknown[]): void => {
  vi.spyOn({ useMeasurements }, "useMeasurements").mockReturnValue({
    data,
    error: undefined,
    isLoading: false
  } as never);
};

describe("MeasurementCard", () => {
  it("renders the most recent reading", () => {
    mockUseMeasurements([
      { registeredAt: "2017-07-21T18:00:00Z", temp: 22.6 },
      { registeredAt: "2017-07-21T17:00:00Z", temp: 21.4 }
    ]);

    renderWithProviders(
      <MeasurementCard
        resource="temperature"
        query={query}
        title="Temperature"
        icon={faTemperatureHalf}
        formatValue={RESOURCE_CONFIG.temperature.formatValue}
      />
    );

    expect(screen.getByText("22.6°C")).toBeInTheDocument();
  });

  it("links to the resource's full history", () => {
    mockUseMeasurements([{ registeredAt: "2017-07-21T17:00:00Z", temp: 21.4 }]);

    renderWithProviders(
      <MeasurementCard
        resource="temperature"
        query={query}
        title="Temperature"
        icon={faTemperatureHalf}
        formatValue={() => ""}
      />
    );

    expect(screen.getByRole("link", { name: /view full history/i })).toHaveAttribute("href", "/history/temperature");
  });

  it("shows an empty state when there are no readings yet", () => {
    mockUseMeasurements([]);

    renderWithProviders(
      <MeasurementCard resource="battery" query={query} title="Battery" icon={faBatteryFull} formatValue={() => ""} />
    );

    expect(screen.getByText("No readings yet for today.")).toBeInTheDocument();
  });

  it("announces a fetch error to assistive technology", () => {
    vi.spyOn({ useMeasurements }, "useMeasurements").mockReturnValue({
      data: undefined,
      error: { status: 500, title: "Server error", detail: "Network error", errors: [] },
      isLoading: false
    } as never);

    renderWithProviders(
      <MeasurementCard resource="battery" query={query} title="Battery" icon={faBatteryFull} formatValue={() => ""} />
    );

    expect(screen.getByRole("alert")).toHaveTextContent("Network error");
  });
});
