import { describe, expect, it, vi } from "vitest";
import type { ReadingsRepository } from "../features/readings/data/readingsRepository";
import { getHistoricalReadings } from "../features/readings/services/getHistoricalReadings";

describe("getHistoricalReadings", () => {
  it("queries only selected metrics and returns merged payload", async () => {
    const getBatteryHistory = vi
      .fn()
      .mockResolvedValue([{ registeredAt: "2026-06-01T10:00:00.000Z", batteryStatus: 58 }]);
    const getTemperatureHistory = vi.fn().mockResolvedValue([{ registeredAt: "2026-06-01T10:00:00.000Z", temp: 22.5 }]);
    const getHumidityHistory = vi.fn().mockResolvedValue([]);

    const repository: ReadingsRepository = {
      getBatteryHistory,
      getTemperatureHistory,
      getHumidityHistory
    };

    const response = await getHistoricalReadings(repository, {
      registeredAt: "2026-06-01T00:00:00.000Z",
      dateOnly: true,
      selectedMetrics: {
        battery: true,
        temperature: true,
        humidity: false
      }
    });

    expect(getBatteryHistory).toHaveBeenCalledTimes(1);
    expect(getTemperatureHistory).toHaveBeenCalledTimes(1);
    expect(getHumidityHistory).not.toHaveBeenCalled();
    expect(response).toEqual({
      battery: [{ registeredAt: "2026-06-01T10:00:00.000Z", batteryStatus: 58 }],
      temperature: [{ registeredAt: "2026-06-01T10:00:00.000Z", temp: 22.5 }],
      humidity: []
    });
  });
});
