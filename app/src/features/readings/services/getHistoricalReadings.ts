import type { ReadingsRepository } from "../data/readingsRepository";
import type { HistoricalReadings, HistoricalSearchRequest } from "../model/readingTypes";

export const getHistoricalReadings = async (
  repository: ReadingsRepository,
  request: HistoricalSearchRequest
): Promise<HistoricalReadings> => {
  const [battery, temperature, humidity] = await Promise.all([
    request.selectedMetrics.battery ? repository.getBatteryHistory(request) : Promise.resolve([]),
    request.selectedMetrics.temperature ? repository.getTemperatureHistory(request) : Promise.resolve([]),
    request.selectedMetrics.humidity ? repository.getHumidityHistory(request) : Promise.resolve([])
  ]);

  return {
    battery,
    temperature,
    humidity
  };
};
