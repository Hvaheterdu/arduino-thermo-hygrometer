export type SensorResource = "battery" | "humidity" | "temperature";

export interface SensorConfig {
  label: string;
  unit: string;
  min: number;
  max: number;
  step: string;
}
