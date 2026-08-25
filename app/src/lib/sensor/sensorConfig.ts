import type { SensorConfig, SensorResource } from "../../types/sensor";

export const SENSOR_CONFIG: Record<SensorResource, SensorConfig> = {
  battery: {
    label: "Battery",
    max: 100,
    min: 0,
    step: "1",
    unit: "%"
  },
  humidity: {
    label: "Humidity",
    max: 90,
    min: 20,
    step: "0.1",
    unit: "% RH"
  },
  temperature: {
    label: "Temperature",
    max: 125,
    min: -55,
    step: "0.1",
    unit: "°C"
  }
};

export const SENSOR_OPTIONS = Object.entries(SENSOR_CONFIG).map(([value, config]) => ({
  label: config.label,
  value: value as SensorResource
}));

export const isSensorResource = (value: string | null): value is SensorResource =>
  value !== null && Object.hasOwn(SENSOR_CONFIG, value);
