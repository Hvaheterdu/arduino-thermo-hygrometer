import type {
  DailySnapshot,
  DailyTrendPoint,
  SensorHealthInsights,
  TrendDirection,
  TrendSummary,
  TrendsInsights
} from "../model/insightTypes";

const average = (values: readonly number[]): number | undefined => {
  if (values.length === 0) {
    return undefined;
  }

  return values.reduce((accumulator, value) => accumulator + value, 0) / values.length;
};

const toDirection = (delta: number | undefined): TrendDirection => {
  if (delta === undefined) {
    return "unknown";
  }

  if (Math.abs(delta) < 0.1) {
    return "steady";
  }

  return delta > 0 ? "up" : "down";
};

const createSummary = (label: string, values: (number | undefined)[]): TrendSummary => {
  const firstDefined = values.find((value) => value !== undefined);
  const lastDefined = [...values].reverse().find((value) => value !== undefined);

  const delta = firstDefined !== undefined && lastDefined !== undefined ? lastDefined - firstDefined : undefined;

  return {
    label,
    ...(lastDefined !== undefined ? { currentValue: lastDefined } : {}),
    ...(delta !== undefined ? { deltaValue: delta } : {}),
    direction: toDirection(delta)
  };
};

export const buildTrendPoints = (snapshots: readonly DailySnapshot[]): DailyTrendPoint[] => {
  return snapshots.map((snapshot) => {
    const batteryAverage = average(snapshot.readings.battery.map((item) => item.batteryStatus));
    const temperatureAverage = average(snapshot.readings.temperature.map((item) => item.temp));
    const humidityAverage = average(snapshot.readings.humidity.map((item) => item.airHumidity));

    return {
      dayLabel: snapshot.dayLabel,
      ...(batteryAverage !== undefined ? { batteryAverage } : {}),
      ...(temperatureAverage !== undefined ? { temperatureAverage } : {}),
      ...(humidityAverage !== undefined ? { humidityAverage } : {}),
      sampleCount:
        snapshot.readings.battery.length + snapshot.readings.temperature.length + snapshot.readings.humidity.length
    };
  });
};

export const buildTrendsInsights = (snapshots: readonly DailySnapshot[]): TrendsInsights => {
  const points = buildTrendPoints(snapshots);

  return {
    points,
    battery: createSummary(
      "Battery",
      points.map((point) => point.batteryAverage)
    ),
    temperature: createSummary(
      "Temperature",
      points.map((point) => point.temperatureAverage)
    ),
    humidity: createSummary(
      "Humidity",
      points.map((point) => point.humidityAverage)
    ),
    failedDays: snapshots.filter((snapshot) => snapshot.hasError).length
  };
};

const getLatestTimestamp = (snapshots: readonly DailySnapshot[]): string | null => {
  const timestamps = snapshots.flatMap((snapshot) => {
    return [
      ...snapshot.readings.battery.map((item) => item.registeredAt),
      ...snapshot.readings.temperature.map((item) => item.registeredAt),
      ...snapshot.readings.humidity.map((item) => item.registeredAt)
    ];
  });

  if (timestamps.length === 0) {
    return null;
  }

  return timestamps.sort().at(-1) ?? null;
};

export const buildSensorHealthInsights = (snapshots: readonly DailySnapshot[]): SensorHealthInsights => {
  const totalDays = snapshots.length;
  const failedDays = snapshots.filter((snapshot) => snapshot.hasError).length;
  const successfulDays = totalDays - failedDays;

  const emptyDays = snapshots.filter((snapshot) => {
    return (
      !snapshot.hasError &&
      snapshot.readings.battery.length === 0 &&
      snapshot.readings.temperature.length === 0 &&
      snapshot.readings.humidity.length === 0
    );
  }).length;

  const daysWithReadings = successfulDays - emptyDays;

  const latestRegisteredAt = getLatestTimestamp(snapshots);
  const minutesSinceLatest =
    latestRegisteredAt === null ? null : (
      Math.max(0, Math.round((Date.now() - Date.parse(latestRegisteredAt)) / 60_000))
    );

  const coveragePercent = totalDays === 0 ? 0 : Math.round((daysWithReadings / totalDays) * 100);
  const apiReliabilityPercent = totalDays === 0 ? 0 : Math.round((successfulDays / totalDays) * 100);

  return {
    latestRegisteredAt,
    minutesSinceLatest,
    coveragePercent,
    apiReliabilityPercent,
    successfulDays,
    emptyDays,
    failedDays,
    totalDays
  };
};
