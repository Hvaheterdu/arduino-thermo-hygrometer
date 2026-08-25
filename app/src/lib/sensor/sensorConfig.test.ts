import { describe, expect, it } from "vitest";

import { isSensorResource, SENSOR_CONFIG, SENSOR_OPTIONS } from "./sensorConfig";

describe("sensor configuration", () => {
  it("defines every supported sensor", () => {
    expect(SENSOR_OPTIONS.map((option) => option.value)).toEqual(["battery", "humidity", "temperature"]);
  });

  it("defines display and validation values", () => {
    expect(SENSOR_CONFIG.temperature).toEqual({
      label: "Temperature",
      max: 125,
      min: -55,
      step: "0.1",
      unit: "°C"
    });
    expect(SENSOR_CONFIG.humidity.unit).toBe("% RH");
    expect(SENSOR_CONFIG.battery.unit).toBe("%");
  });

  it("validates sensor query values", () => {
    expect(isSensorResource("temperature")).toBe(true);
    expect(isSensorResource("humidity")).toBe(true);
    expect(isSensorResource("battery")).toBe(true);
    expect(isSensorResource("unknown")).toBe(false);
    expect(isSensorResource("toString")).toBe(false);
    expect(isSensorResource(null)).toBe(false);
  });
});
