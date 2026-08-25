import type { LoaderFunctionArgs } from "react-router";
import { describe, expect, it, vi } from "vitest";

import {
  getBatteriesByDateOrTimestamp,
  getHumiditiesByDateOrTimestamp,
  getTemperaturesByDateOrTimestamp
} from "../../lib";
import { historyLoader } from "./history.loader";

vi.mock("../../lib", async (importOriginal) => {
  const actual = await importOriginal<typeof import("../../lib")>();

  return {
    ...actual,
    dateToRegisteredAt: vi.fn((date: string) => `${date}T00:00:00`),
    getBatteriesByDateOrTimestamp: vi.fn(),
    getHumiditiesByDateOrTimestamp: vi.fn(),
    getTemperaturesByDateOrTimestamp: vi.fn(),
    getToday: vi.fn(() => "2026-08-25"),
    isValidDate: vi.fn((value: string) => value === "2026-08-24")
  };
});

const loaderArgs = (url: string): LoaderFunctionArgs =>
  ({
    context: {},
    params: {},
    request: new Request(url)
  }) as unknown as LoaderFunctionArgs;

describe("historyLoader", () => {
  it("loads battery readings", async () => {
    const readings = [
      {
        batteryStatus: 80,
        registeredAt: "2026-08-24T12:00:00"
      }
    ];

    vi.mocked(getBatteriesByDateOrTimestamp).mockResolvedValue(readings);

    await expect(
      historyLoader(loaderArgs("http://localhost/history?resource=battery&date=2026-08-24"))
    ).resolves.toEqual({
      date: "2026-08-24",
      readings,
      registeredAt: "2026-08-24T00:00:00",
      resource: "battery"
    });
  });

  it("loads humidity readings", async () => {
    const readings = [
      {
        airHumidity: 54,
        registeredAt: "2026-08-24T12:00:00"
      }
    ];

    vi.mocked(getHumiditiesByDateOrTimestamp).mockResolvedValue(readings);

    await expect(
      historyLoader(loaderArgs("http://localhost/history?resource=humidity&date=2026-08-24"))
    ).resolves.toEqual({
      date: "2026-08-24",
      readings,
      registeredAt: "2026-08-24T00:00:00",
      resource: "humidity"
    });
  });

  it("loads temperature readings", async () => {
    const readings = [
      {
        registeredAt: "2026-08-24T12:00:00",
        temp: 21.5
      }
    ];

    vi.mocked(getTemperaturesByDateOrTimestamp).mockResolvedValue(readings);

    await expect(
      historyLoader(loaderArgs("http://localhost/history?resource=temperature&date=2026-08-24"))
    ).resolves.toEqual({
      date: "2026-08-24",
      readings,
      registeredAt: "2026-08-24T00:00:00",
      resource: "temperature"
    });
  });

  it("defaults to temperature for an unknown sensor", async () => {
    const readings = [
      {
        registeredAt: "2026-08-24T12:00:00",
        temp: 21.5
      }
    ];

    vi.mocked(getTemperaturesByDateOrTimestamp).mockResolvedValue(readings);

    await expect(historyLoader(loaderArgs("http://localhost/history?resource=unknown"))).resolves.toMatchObject({
      readings,
      resource: "temperature"
    });
  });
});
