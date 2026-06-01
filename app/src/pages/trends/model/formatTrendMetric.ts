import { formatDecimal } from "../../../features/readings/model/formatters";

export const formatTrendMetricValue = (metricValue: number | undefined, metricUnitSuffix: string): string => {
  if (metricValue === undefined) {
    return "No data";
  }

  return `${formatDecimal(metricValue)} ${metricUnitSuffix}`;
};

export const formatTrendMetricDeltaValue = (metricDeltaValue: number | undefined, metricUnitSuffix: string): string => {
  if (metricDeltaValue === undefined) {
    return "No baseline";
  }

  const deltaSignPrefix = metricDeltaValue > 0 ? "+" : "";
  return `${deltaSignPrefix}${formatDecimal(metricDeltaValue)} ${metricUnitSuffix}`;
};
