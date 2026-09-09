import type { components } from "@/arduino-thermo-hygrometer-api";

export type BatteryDto = components["schemas"]["BatteryDto"];

export type HumidityDto = components["schemas"]["HumidityDto"];

export type ResourceKind = "battery" | "humidity" | "temperature";

export type MeasurementDto = BatteryDto | HumidityDto | TemperatureDto;
export type MeasurementQuery = {
  registeredAt: string;
  dateOnly: boolean;
};
export type MeasurementDtoMap = {
  battery: BatteryDto;
  humidity: HumidityDto;
  temperature: TemperatureDto;
};

export type TemperatureDto = components["schemas"]["TemperatureDto"];
