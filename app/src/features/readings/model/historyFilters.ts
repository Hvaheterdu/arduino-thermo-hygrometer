import type { HistoricalSearchRequest, MetricSelection, NumericBounds, SearchFilters } from "./readingTypes";

export type TimeMode = "day" | "timestamp";

export interface FilterDraft {
  readonly timeMode: TimeMode;
  readonly dayValue: string;
  readonly timestampValue: string;
  readonly searchText: string;
  readonly selectedMetrics: MetricSelection;
  readonly batteryBounds: NumericBounds;
  readonly temperatureBounds: NumericBounds;
  readonly humidityBounds: NumericBounds;
}

export interface SubmittedFilters {
  readonly request: HistoricalSearchRequest;
  readonly searchFilters: SearchFilters;
}

const now = new Date();

const toDateInput = (date: Date): string => {
  const year = String(date.getFullYear());
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

const toDateTimeLocalInput = (date: Date): string => {
  const timezoneOffset = date.getTimezoneOffset() * 60000;
  const localDate = new Date(date.getTime() - timezoneOffset);
  return localDate.toISOString().slice(0, 16);
};

export const initialFilterDraft: FilterDraft = {
  timeMode: "day",
  dayValue: toDateInput(now),
  timestampValue: toDateTimeLocalInput(now),
  searchText: "",
  selectedMetrics: {
    battery: true,
    temperature: true,
    humidity: true
  },
  batteryBounds: {},
  temperatureBounds: {},
  humidityBounds: {}
};

const hasAtLeastOneMetric = (selection: MetricSelection): boolean => {
  return selection.battery || selection.temperature || selection.humidity;
};

const toIsoTimestamp = (value: string): string => {
  if (value.length === 0) {
    return new Date().toISOString();
  }

  return new Date(value).toISOString();
};

export const toSubmittedFilters = (draft: FilterDraft): SubmittedFilters => {
  const selectedMetrics =
    hasAtLeastOneMetric(draft.selectedMetrics) ?
      draft.selectedMetrics
    : { battery: true, temperature: true, humidity: true };

  const request: HistoricalSearchRequest =
    draft.timeMode === "day" ?
      {
        registeredAt: toIsoTimestamp(`${draft.dayValue}T00:00`),
        dateOnly: true,
        selectedMetrics
      }
    : {
        registeredAt: toIsoTimestamp(draft.timestampValue),
        dateOnly: false,
        selectedMetrics
      };

  return {
    request,
    searchFilters: {
      searchText: draft.searchText,
      batteryBounds: draft.batteryBounds,
      temperatureBounds: draft.temperatureBounds,
      humidityBounds: draft.humidityBounds
    }
  };
};
