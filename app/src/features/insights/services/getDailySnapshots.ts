import { frontendLogger } from "../../../shared/observability/logger";
import { defaultReadingsRepository, type ReadingsRepository } from "../../readings/data/readingsRepository";
import type { HistoricalReadings } from "../../readings/model/readingTypes";
import { getHistoricalReadings } from "../../readings/services/getHistoricalReadings";
import type { DailySnapshot } from "../model/insightTypes";

const createDayLabel = (date: Date): string => {
  return new Intl.DateTimeFormat("en-GB", { day: "2-digit", month: "short" }).format(date);
};

const createDayKey = (date: Date): string => {
  return date.toISOString().slice(0, 10);
};

const createRecentDates = (days: number): Date[] => {
  const result: Date[] = [];

  for (let index = days - 1; index >= 0; index -= 1) {
    const date = new Date();
    date.setHours(0, 0, 0, 0);
    date.setDate(date.getDate() - index);
    result.push(date);
  }

  return result;
};

const emptyReadings: HistoricalReadings = {
  battery: [],
  temperature: [],
  humidity: []
};

export const getDailySnapshots = async (
  days: number,
  repository: ReadingsRepository = defaultReadingsRepository
): Promise<DailySnapshot[]> => {
  const dates = createRecentDates(days);

  const settled = await Promise.allSettled(
    dates.map(async (date) => {
      const request = {
        registeredAt: date.toISOString(),
        dateOnly: true,
        selectedMetrics: {
          battery: true,
          temperature: true,
          humidity: true
        }
      } as const;

      const readings = await getHistoricalReadings(repository, request);
      return {
        dayLabel: createDayLabel(date),
        dayKey: createDayKey(date),
        readings
      };
    })
  );

  return settled.map((result, index) => {
    if (result.status === "fulfilled") {
      return {
        ...result.value,
        hasError: false
      };
    }

    const fallbackDate = dates[index];
    if (fallbackDate === undefined) {
      return { dayLabel: "Unknown", dayKey: "unknown", readings: emptyReadings, hasError: true };
    }

    frontendLogger.warn("Unable to load daily snapshot for trend insights.", {
      dayKey: createDayKey(fallbackDate),
      reason: result.reason instanceof Error ? result.reason.message : "Unknown snapshot error"
    });

    return {
      dayLabel: createDayLabel(fallbackDate),
      dayKey: createDayKey(fallbackDate),
      readings: emptyReadings,
      hasError: true
    };
  });
};
