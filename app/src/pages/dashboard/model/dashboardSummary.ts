import { formatDecimal } from "../../../features/readings/model/formatters";
import type {
  BatteryReading,
  HistoricalReadings,
  HumidityReading,
  TemperatureReading
} from "../../../features/readings/model/readingTypes";

export interface DashboardSummaryValues {
  readonly batteryLatestValue: string;
  readonly batteryReadingCountDescription: string;
  readonly temperatureLatestValue: string;
  readonly temperatureReadingCountDescription: string;
  readonly humidityLatestValue: string;
  readonly humidityReadingCountDescription: string;
}

const formatReadingCountDescription = (readingCount: number): string => {
  return `${String(readingCount)} ${readingCount === 1 ? "reading" : "readings"}`;
};

const formatLatestBatteryValue = (batteryReadings: readonly BatteryReading[]): string => {
  const latestBatteryReading = batteryReadings.at(-1);
  return latestBatteryReading ? `${formatDecimal(latestBatteryReading.batteryStatus, 0)} %` : "No data";
};

const formatLatestTemperatureValue = (temperatureReadings: readonly TemperatureReading[]): string => {
  const latestTemperatureReading = temperatureReadings.at(-1);
  return latestTemperatureReading ? `${formatDecimal(latestTemperatureReading.temp)} °C` : "No data";
};

const formatLatestHumidityValue = (humidityReadings: readonly HumidityReading[]): string => {
  const latestHumidityReading = humidityReadings.at(-1);
  return latestHumidityReading ? `${formatDecimal(latestHumidityReading.airHumidity)} %` : "No data";
};

export const buildDashboardSummaryValues = (
  historicalReadings: HistoricalReadings | undefined
): DashboardSummaryValues => {
  const batteryReadings = historicalReadings?.battery ?? [];
  const temperatureReadings = historicalReadings?.temperature ?? [];
  const humidityReadings = historicalReadings?.humidity ?? [];

  return {
    batteryLatestValue: formatLatestBatteryValue(batteryReadings),
    batteryReadingCountDescription: formatReadingCountDescription(batteryReadings.length),
    temperatureLatestValue: formatLatestTemperatureValue(temperatureReadings),
    temperatureReadingCountDescription: formatReadingCountDescription(temperatureReadings.length),
    humidityLatestValue: formatLatestHumidityValue(humidityReadings),
    humidityReadingCountDescription: formatReadingCountDescription(humidityReadings.length)
  };
};
