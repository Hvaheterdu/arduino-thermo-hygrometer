import type { BatteryDto, HumidityDto, TemperatureDto } from "./apiSchema";

export interface HistoryRequest {
  readonly registeredAt: string;
  readonly dateOnly: boolean;
}

export interface ReadingsApi {
  getBatteries(request: HistoryRequest): Promise<BatteryDto[]>;
  getTemperatures(request: HistoryRequest): Promise<TemperatureDto[]>;
  getHumidities(request: HistoryRequest): Promise<HumidityDto[]>;
}
