import type { LoaderFunctionArgs } from "react-router";
import { describe, expect, it, vi } from "vitest";

import {
  getBatteriesByDateOrTimestamp,
  getHumiditiesByDateOrTimestamp,
  getTemperaturesByDateOrTimestamp
} from "../../lib";
import { dashboardLoader } from "./dashboard.loader";

vi.mock("../../lib", () => ({
  dateToRegisteredAt: vi.fn((date: string) => `${date}T00:00:00`),
  getBatteriesByDateOrTimestamp: vi.fn(),
  getHumiditiesByDateOrTimestamp: vi.fn(),
  getTemperaturesByDateOrTimestamp: vi.fn(),
  getToday: vi.fn(() => "2026-08-25"),
  isValidDate: vi.fn((date: string) => date === "2026-08-24")
}));

const loaderArgs = (url: string) => {
  return {
    context: {},
    params: {},
    request: new Request(url)
  } as LoaderFunctionArgs;
};

const mockReadings = () => {
  vi.mocked(getBatteriesByDateOrTimestamp).mockResolvedValue([]);
  vi.mocked(getHumiditiesByDateOrTimestamp).mockResolvedValue([]);
  vi.mocked(getTemperaturesByDateOrTimestamp).mockResolvedValue([]);
};

describe("dashboardLoader", () => {
  it("loads all sensors for the requested date", async () => {
    const battery = [{ batteryStatus: 80, registeredAt: "2026-08-24T12:00:00" }];
    const humidity = [{ airHumidity: 54, registeredAt: "2026-08-24T12:00:00" }];
    const temperature = [{ registeredAt: "2026-08-24T12:00:00", temp: 21.5 }];

    vi.mocked(getBatteriesByDateOrTimestamp).mockResolvedValue(battery);
    vi.mocked(getHumiditiesByDateOrTimestamp).mockResolvedValue(humidity);
    vi.mocked(getTemperaturesByDateOrTimestamp).mockResolvedValue(temperature);

    await expect(dashboardLoader(loaderArgs("http://localhost/?date=2026-08-24"))).resolves.toEqual({
      battery,
      date: "2026-08-24",
      humidity,
      registeredAt: "2026-08-24T00:00:00",
      temperature
    });
    expect(getBatteriesByDateOrTimestamp).toHaveBeenCalledWith(
      expect.objectContaining({
        dateOnly: true,
        registeredAt: "2026-08-24T00:00:00"
      })
    );
  });

  it("falls back to today for an invalid date", async () => {
    mockReadings();

    await expect(dashboardLoader(loaderArgs("http://localhost/?date=invalid"))).resolves.toMatchObject({
      date: "2026-08-25",
      registeredAt: "2026-08-25T00:00:00"
    });
  });
});
