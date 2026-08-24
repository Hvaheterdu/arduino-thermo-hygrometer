import type { SensorConfig, SensorResource } from "@/types/sensor";

export const SENSOR_CONFIG: Record<SensorResource, SensorConfig> = {
  temperature: { label: "Temperature", min: -55, max: 125, step: "0.1", unit: "°C" },
  humidity: { label: "Humidity", min: 20, max: 90, step: "0.1", unit: "% RH" },
  battery: { label: "Battery", min: 0, max: 100, step: "1", unit: "%" }
};

type SensorOption = {
  value: SensorResource;
  label: string;
};

export const SENSOR_OPTIONS: SensorOption[] = Object.entries(SENSOR_CONFIG).map(([value, config]) => ({
  value: value as SensorResource,
  label: config.label
}));

export const isSensorResource = (value: string | null): value is SensorResource => value !== null && value in SENSOR_CONFIG;
