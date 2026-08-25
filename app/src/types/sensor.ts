export type SensorResource = "battery" | "humidity" | "temperature";

export type SensorConfig = {
  label: string;
  unit: string;
  min: number;
  max: number;
  step: string;
};
