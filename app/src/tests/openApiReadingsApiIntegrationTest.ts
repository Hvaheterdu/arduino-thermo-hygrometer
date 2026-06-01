import { describe, expect, it } from "vitest";
import { OpenApiReadingsApi, type OpenApiClientLike } from "../features/readings/data/openApiReadingsApi";
import { ApiError } from "../shared/api/apiError";

type MockGet = OpenApiClientLike["GET"];

describe("OpenApiReadingsApi", () => {
  it("maps success responses for batteries", async () => {
    let callCount = 0;
    const getMock: MockGet = () => {
      callCount += 1;

      return Promise.resolve({
        data: [{ id: "battery-1", registeredAt: "2026-06-01T10:00:00.000Z", batteryStatus: 67 }]
      });
    };

    const api = new OpenApiReadingsApi({ GET: getMock });
    const batteries = await api.getBatteries({
      registeredAt: "2026-06-01T00:00:00.000Z",
      dateOnly: true
    });

    expect(callCount).toBe(1);
    expect(batteries).toEqual([{ id: "battery-1", registeredAt: "2026-06-01T10:00:00.000Z", batteryStatus: 67 }]);
  });

  it("throws ApiError when API returns problem details", async () => {
    const getMock: MockGet = () => {
      return Promise.resolve({
        error: {
          type: "https://api.arduinothermohygrometer/errors/unauthorized",
          title: "Unauthorized.",
          detail: "Missing or invalid API key.",
          status: 401,
          instance: "/api/v1/temperatures",
          traceId: "trace-123",
          timestamp: "2026-06-01T10:00:00.000Z"
        }
      });
    };

    const api = new OpenApiReadingsApi({ GET: getMock });

    await expect(
      api.getTemperatures({
        registeredAt: "2026-06-01T00:00:00.000Z",
        dateOnly: true
      })
    ).rejects.toBeInstanceOf(ApiError);
  });
});
