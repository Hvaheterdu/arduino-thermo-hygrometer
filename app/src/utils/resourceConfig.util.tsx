import type { IconDefinition } from "@fortawesome/fontawesome-svg-core";
import { faBatteryFull, faDroplet, faTemperatureHalf } from "@fortawesome/free-solid-svg-icons";

import type { BatteryDto, HumidityDto, MeasurementDto, ResourceKind, TemperatureDto } from "@/types/measurement.types";

export type ResourceConfig = {
  title: string;
  icon: IconDefinition;
  fieldName: string;
  fieldLabel: string;
  unit: string;
  min: number;
  max: number;
  step?: number;
  formatValue: (measurement: MeasurementDto) => string;
};

export const RESOURCE_CONFIG: Record<ResourceKind, ResourceConfig> = {
  battery: {
    title: "Battery",
    icon: faBatteryFull,
    fieldName: "batteryStatus",
    fieldLabel: "Battery status",
    unit: "%",
    min: 0,
    max: 100,
    step: 1,
    formatValue: (measurement) => `${(measurement as BatteryDto).batteryStatus}%`
  },
  humidity: {
    title: "Humidity",
    icon: faDroplet,
    fieldName: "airHumidity",
    fieldLabel: "Air humidity",
    unit: "%",
    min: 20,
    max: 90,
    formatValue: (measurement) => `${(measurement as HumidityDto).airHumidity.toFixed(1)}%`
  },
  temperature: {
    title: "Temperature",
    icon: faTemperatureHalf,
    fieldName: "temp",
    fieldLabel: "Temperature",
    unit: "°C",
    min: -55,
    max: 125,
    formatValue: (measurement) => `${(measurement as TemperatureDto).temp.toFixed(1)}°C`
  }
};
