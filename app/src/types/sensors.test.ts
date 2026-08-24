import { describe, expect, it } from "vitest";

import { isSensorResource, SENSOR_CONFIG } from "@/types/sensors";

describe("sensor configuration", () => {
  it("defines a display and validation configuration for every sensor", () => {
    expect(Object.keys(SENSOR_CONFIG)).toHaveLength(3);
    expect(SENSOR_CONFIG.temperature.unit).toBe("°C");
    expect(SENSOR_CONFIG.humidity.unit).toBe("% RH");
    expect(SENSOR_CONFIG.battery.unit).toBe("%");
  });

  it("validates sensor query values", () => {
    expect(isSensorResource("temperature")).toBe(true);
    expect(isSensorResource("humidity")).toBe(true);
    expect(isSensorResource("battery")).toBe(true);
    expect(isSensorResource("unknown")).toBe(false);
    expect(isSensorResource(null)).toBe(false);
  });
});
