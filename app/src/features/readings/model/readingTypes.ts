export type SensorType = "battery" | "temperature" | "humidity";

export interface ReadingBase {
  readonly id?: string;
  readonly registeredAt: string;
}

export interface BatteryReading extends ReadingBase {
  readonly batteryStatus: number;
}

export interface TemperatureReading extends ReadingBase {
  readonly temp: number;
}

export interface HumidityReading extends ReadingBase {
  readonly airHumidity: number;
}

export interface MetricSelection {
  readonly battery: boolean;
  readonly temperature: boolean;
  readonly humidity: boolean;
}

export interface NumericBounds {
  readonly min?: number;
  readonly max?: number;
}

export interface SearchFilters {
  readonly searchText: string;
  readonly batteryBounds: NumericBounds;
  readonly temperatureBounds: NumericBounds;
  readonly humidityBounds: NumericBounds;
}

export interface HistoricalSearchRequest {
  readonly registeredAt: string;
  readonly dateOnly: boolean;
  readonly selectedMetrics: MetricSelection;
}

export interface HistoricalReadings {
  readonly battery: BatteryReading[];
  readonly temperature: TemperatureReading[];
  readonly humidity: HumidityReading[];
}
