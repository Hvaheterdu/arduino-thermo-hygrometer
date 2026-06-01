export interface TrendMetricVisibility {
  readonly battery: boolean;
  readonly temperature: boolean;
  readonly humidity: boolean;
}

export type TrendMetricVisibilityKey = keyof TrendMetricVisibility;

const metricVisibilityQueryParameterByMetricKey: Record<TrendMetricVisibilityKey, string> = {
  battery: "b",
  temperature: "t",
  humidity: "h"
};

const isMetricVisibleFromQueryParameterValue = (queryParameterValue: string | null): boolean => {
  return queryParameterValue === null ? true : queryParameterValue !== "0";
};

export const readTrendMetricVisibilityFromSearchParams = (
  currentSearchParams: URLSearchParams
): TrendMetricVisibility => {
  return {
    battery: isMetricVisibleFromQueryParameterValue(
      currentSearchParams.get(metricVisibilityQueryParameterByMetricKey.battery)
    ),
    temperature: isMetricVisibleFromQueryParameterValue(
      currentSearchParams.get(metricVisibilityQueryParameterByMetricKey.temperature)
    ),
    humidity: isMetricVisibleFromQueryParameterValue(
      currentSearchParams.get(metricVisibilityQueryParameterByMetricKey.humidity)
    )
  };
};

export const buildSearchParamsWithUpdatedTrendMetricVisibility = (
  currentSearchParams: URLSearchParams,
  metricVisibilityKey: TrendMetricVisibilityKey,
  isMetricVisible: boolean
): URLSearchParams => {
  const nextSearchParams = new URLSearchParams(currentSearchParams);
  const metricVisibilityQueryParameter = metricVisibilityQueryParameterByMetricKey[metricVisibilityKey];

  if (isMetricVisible) {
    nextSearchParams.delete(metricVisibilityQueryParameter);
  } else {
    nextSearchParams.set(metricVisibilityQueryParameter, "0");
  }

  return nextSearchParams;
};

export const hasAtLeastOneVisibleTrendMetric = (metricVisibility: TrendMetricVisibility): boolean => {
  return metricVisibility.battery || metricVisibility.temperature || metricVisibility.humidity;
};
