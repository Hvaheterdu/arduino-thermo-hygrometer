import { describe, expect, it } from "vitest";
import type { DailySnapshot } from "../features/insights/model/insightTypes";
import {
  buildSensorHealthInsights,
  buildTrendPoints,
  buildTrendsInsights
} from "../features/insights/services/buildInsights";

const snapshots: DailySnapshot[] = [
  {
    dayLabel: "30 May",
    dayKey: "2026-05-30",
    hasError: false,
    readings: {
      battery: [{ registeredAt: "2026-05-30T10:00:00.000Z", batteryStatus: 70 }],
      temperature: [{ registeredAt: "2026-05-30T10:00:00.000Z", temp: 20.2 }],
      humidity: [{ registeredAt: "2026-05-30T10:00:00.000Z", airHumidity: 45.1 }]
    }
  },
  {
    dayLabel: "31 May",
    dayKey: "2026-05-31",
    hasError: false,
    readings: {
      battery: [{ registeredAt: "2026-05-31T10:00:00.000Z", batteryStatus: 65 }],
      temperature: [{ registeredAt: "2026-05-31T10:00:00.000Z", temp: 21.8 }],
      humidity: [{ registeredAt: "2026-05-31T10:00:00.000Z", airHumidity: 52.4 }]
    }
  },
  {
    dayLabel: "01 Jun",
    dayKey: "2026-06-01",
    hasError: true,
    readings: {
      battery: [],
      temperature: [],
      humidity: []
    }
  }
];

describe("buildInsights", () => {
  it("builds trend points and trend summaries", () => {
    const points = buildTrendPoints(snapshots);
    const trends = buildTrendsInsights(snapshots);

    expect(points).toHaveLength(3);
    expect(trends.failedDays).toBe(1);
    expect(trends.battery.direction).toBe("down");
    expect(trends.temperature.direction).toBe("up");
  });

  it("builds sensor health indicators", () => {
    const health = buildSensorHealthInsights(snapshots);

    expect(health.totalDays).toBe(3);
    expect(health.failedDays).toBe(1);
    expect(health.coveragePercent).toBeGreaterThan(0);
    expect(health.apiReliabilityPercent).toBeGreaterThan(0);
  });
});
