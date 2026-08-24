import type { BatteryDto, HumidityDto, TemperatureDto } from "@/types/domain";

export type SensorResource = "battery" | "humidity" | "temperature";

export type SensorConfig = {
  label: string;
  unit: string;
  min: number;
  max: number;
  step: string;
};

export type SensorReading = BatteryDto | HumidityDto | TemperatureDto;
