import type {
  BatteryReading,
  HistoricalReadings,
  HumidityReading,
  NumericBounds,
  SearchFilters,
  TemperatureReading
} from "./readingTypes";

const normalizeSearchText = (searchText: string): string => searchText.trim().toLowerCase();

const matchesTextFilter = (registeredAt: string, id: string | undefined, normalizedSearch: string): boolean => {
  if (normalizedSearch.length === 0) {
    return true;
  }

  return (
    registeredAt.toLowerCase().includes(normalizedSearch) || (id?.toLowerCase().includes(normalizedSearch) ?? false)
  );
};

const isInBounds = (value: number, bounds: NumericBounds): boolean => {
  if (bounds.min !== undefined && value < bounds.min) {
    return false;
  }

  if (bounds.max !== undefined && value > bounds.max) {
    return false;
  }

  return true;
};

export const filterBatteryReadings = (
  readings: readonly BatteryReading[],
  searchText: string,
  bounds: NumericBounds
): BatteryReading[] => {
  const normalizedSearch = normalizeSearchText(searchText);

  return readings.filter((reading) => {
    return (
      matchesTextFilter(reading.registeredAt, reading.id, normalizedSearch) && isInBounds(reading.batteryStatus, bounds)
    );
  });
};

export const filterTemperatureReadings = (
  readings: readonly TemperatureReading[],
  searchText: string,
  bounds: NumericBounds
): TemperatureReading[] => {
  const normalizedSearch = normalizeSearchText(searchText);

  return readings.filter((reading) => {
    return matchesTextFilter(reading.registeredAt, reading.id, normalizedSearch) && isInBounds(reading.temp, bounds);
  });
};

export const filterHumidityReadings = (
  readings: readonly HumidityReading[],
  searchText: string,
  bounds: NumericBounds
): HumidityReading[] => {
  const normalizedSearch = normalizeSearchText(searchText);

  return readings.filter((reading) => {
    return (
      matchesTextFilter(reading.registeredAt, reading.id, normalizedSearch) && isInBounds(reading.airHumidity, bounds)
    );
  });
};

export const applySearchFilters = (readings: HistoricalReadings, filters: SearchFilters): HistoricalReadings => {
  return {
    battery: filterBatteryReadings(readings.battery, filters.searchText, filters.batteryBounds),
    temperature: filterTemperatureReadings(readings.temperature, filters.searchText, filters.temperatureBounds),
    humidity: filterHumidityReadings(readings.humidity, filters.searchText, filters.humidityBounds)
  };
};
