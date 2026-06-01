import type { HistoricalReadings } from "../../readings/model/readingTypes";

export interface DailySnapshot {
  readonly dayLabel: string;
  readonly dayKey: string;
  readonly readings: HistoricalReadings;
  readonly hasError: boolean;
}

export interface DailyTrendPoint {
  readonly dayLabel: string;
  readonly batteryAverage?: number;
  readonly temperatureAverage?: number;
  readonly humidityAverage?: number;
  readonly sampleCount: number;
}

export type TrendDirection = "up" | "down" | "steady" | "unknown";

export interface TrendSummary {
  readonly label: string;
  readonly currentValue?: number;
  readonly deltaValue?: number;
  readonly direction: TrendDirection;
}

export interface TrendsInsights {
  readonly points: DailyTrendPoint[];
  readonly battery: TrendSummary;
  readonly temperature: TrendSummary;
  readonly humidity: TrendSummary;
  readonly failedDays: number;
}

export interface SensorHealthInsights {
  readonly latestRegisteredAt: string | null;
  readonly minutesSinceLatest: number | null;
  readonly coveragePercent: number;
  readonly apiReliabilityPercent: number;
  readonly successfulDays: number;
  readonly emptyDays: number;
  readonly failedDays: number;
  readonly totalDays: number;
}
