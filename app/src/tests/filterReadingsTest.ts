import { describe, expect, it } from "vitest";
import {
  applySearchFilters,
  filterBatteryReadings,
  filterHumidityReadings,
  filterTemperatureReadings
} from "../features/readings/model/filterReadings";

describe("filterReadings", () => {
  it("filters battery readings by text and numeric range", () => {
    const result = filterBatteryReadings(
      [
        { id: "id-1", registeredAt: "2026-06-01T09:30:00.000Z", batteryStatus: 81 },
        { id: "id-2", registeredAt: "2026-06-01T14:30:00.000Z", batteryStatus: 45 }
      ],
      "14:30",
      { min: 40, max: 50 }
    );

    expect(result).toEqual([{ id: "id-2", registeredAt: "2026-06-01T14:30:00.000Z", batteryStatus: 45 }]);
  });

  it("filters temperatures and humidities independently", () => {
    const temperatureResult = filterTemperatureReadings(
      [
        { registeredAt: "2026-06-01T09:30:00.000Z", temp: 18.4 },
        { registeredAt: "2026-06-01T14:30:00.000Z", temp: 30.1 }
      ],
      "",
      { max: 20 }
    );

    const humidityResult = filterHumidityReadings(
      [
        { registeredAt: "2026-06-01T09:30:00.000Z", airHumidity: 35.2 },
        { registeredAt: "2026-06-01T14:30:00.000Z", airHumidity: 63.8 }
      ],
      "",
      { min: 50 }
    );

    expect(temperatureResult).toEqual([{ registeredAt: "2026-06-01T09:30:00.000Z", temp: 18.4 }]);
    expect(humidityResult).toEqual([{ registeredAt: "2026-06-01T14:30:00.000Z", airHumidity: 63.8 }]);
  });

  it("applies global search filters to all metric collections", () => {
    const filtered = applySearchFilters(
      {
        battery: [{ id: "a", registeredAt: "2026-06-01T10:00:00.000Z", batteryStatus: 50 }],
        temperature: [{ id: "b", registeredAt: "2026-06-01T10:00:00.000Z", temp: 21.2 }],
        humidity: [{ id: "c", registeredAt: "2026-06-01T11:00:00.000Z", airHumidity: 52.4 }]
      },
      {
        searchText: "10:00",
        batteryBounds: {},
        temperatureBounds: { min: 20, max: 22 },
        humidityBounds: {}
      }
    );

    expect(filtered.battery).toHaveLength(1);
    expect(filtered.temperature).toHaveLength(1);
    expect(filtered.humidity).toHaveLength(0);
  });
});
