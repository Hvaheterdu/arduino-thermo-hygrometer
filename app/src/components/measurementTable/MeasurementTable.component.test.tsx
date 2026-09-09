import { screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";

import { MeasurementTable } from "@/components/measurementTable/MeasurementTable.component";
import { renderWithProviders } from "@/test/render.util";
import { RESOURCE_CONFIG } from "@/utils/resourceConfig.util";

const measurementTableBaseValues = {
  readings: undefined,
  isLoading: false,
  error: undefined,
  valueHeader: "Temp (°C)",
  formatValue: () => ""
};

describe("MeasurementTable", () => {
  it("shows a loading skeleton while fetching", () => {
    renderWithProviders(<MeasurementTable {...measurementTableBaseValues} isLoading />);

    expect(screen.getByTestId("measurement-table-skeleton")).toBeInTheDocument();
  });

  it("shows the error message when the request failed", () => {
    const error = { status: 500, title: "Error", detail: "Something broke.", errors: [] };

    renderWithProviders(<MeasurementTable {...measurementTableBaseValues} error={error} />);

    expect(screen.getByRole("alert")).toHaveTextContent("Something broke.");
  });

  it("shows an empty state when there are no readings", () => {
    renderWithProviders(<MeasurementTable {...measurementTableBaseValues} readings={[]} />);

    expect(screen.getByText("No readings found for this date.")).toBeInTheDocument();
  });

  it("renders one row per reading, formatted with formatValue", () => {
    const readings = [{ registeredAt: "2017-07-21T17:00:00Z", temp: 21.4 } as never];

    renderWithProviders(
      <MeasurementTable
        {...measurementTableBaseValues}
        readings={readings}
        formatValue={RESOURCE_CONFIG.temperature.formatValue}
      />
    );

    expect(screen.getByText("21.4°C")).toBeInTheDocument();
  });
});
