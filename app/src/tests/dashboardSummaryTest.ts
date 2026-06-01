import { describe, expect, it } from "vitest";
import { buildDashboardSummaryValues } from "../pages/dashboard/model/dashboardSummary";

describe("buildDashboardSummaryValues", () => {
  it("returns fallback values when historical readings are missing", () => {
    const dashboardSummaryValues = buildDashboardSummaryValues(undefined);

    expect(dashboardSummaryValues).toEqual({
      batteryLatestValue: "No data",
      batteryReadingCountDescription: "0 readings",
      temperatureLatestValue: "No data",
      temperatureReadingCountDescription: "0 readings",
      humidityLatestValue: "No data",
      humidityReadingCountDescription: "0 readings"
    });
  });

  it("formats latest values and count labels from readings", () => {
    const dashboardSummaryValues = buildDashboardSummaryValues({
      battery: [
        { registeredAt: "2026-06-01T08:00:00.000Z", batteryStatus: 89 },
        { registeredAt: "2026-06-01T09:00:00.000Z", batteryStatus: 87 }
      ],
      temperature: [{ registeredAt: "2026-06-01T09:00:00.000Z", temp: 22.35 }],
      humidity: [{ registeredAt: "2026-06-01T09:00:00.000Z", airHumidity: 41.12 }]
    });

    expect(dashboardSummaryValues.batteryLatestValue).toBe("87 %");
    expect(dashboardSummaryValues.batteryReadingCountDescription).toBe("2 readings");
    expect(dashboardSummaryValues.temperatureLatestValue).toBe("22.35 °C");
    expect(dashboardSummaryValues.temperatureReadingCountDescription).toBe("1 reading");
    expect(dashboardSummaryValues.humidityLatestValue).toBe("41.12 %");
    expect(dashboardSummaryValues.humidityReadingCountDescription).toBe("1 reading");
  });
});
