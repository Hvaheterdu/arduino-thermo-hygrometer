import { useSearchParams } from "react-router";
import {
  buildSearchParamsWithUpdatedTrendMetricVisibility,
  hasAtLeastOneVisibleTrendMetric,
  readTrendMetricVisibilityFromSearchParams,
  type TrendMetricVisibility,
  type TrendMetricVisibilityKey
} from "../model/trendMetricVisibility";

interface UseTrendMetricVisibilityResult {
  readonly trendMetricVisibility: TrendMetricVisibility;
  readonly hasAtLeastOneVisibleMetric: boolean;
  readonly updateTrendMetricVisibility: (
    metricVisibilityKey: TrendMetricVisibilityKey,
    isMetricVisible: boolean
  ) => void;
}

export const useTrendMetricVisibility = (): UseTrendMetricVisibilityResult => {
  const [currentSearchParams, setCurrentSearchParams] = useSearchParams();

  const trendMetricVisibility = readTrendMetricVisibilityFromSearchParams(currentSearchParams);

  const updateTrendMetricVisibility = (
    metricVisibilityKey: TrendMetricVisibilityKey,
    isMetricVisible: boolean
  ): void => {
    const updatedSearchParams = buildSearchParamsWithUpdatedTrendMetricVisibility(
      currentSearchParams,
      metricVisibilityKey,
      isMetricVisible
    );

    setCurrentSearchParams(updatedSearchParams, { replace: true });
  };

  return {
    trendMetricVisibility,
    hasAtLeastOneVisibleMetric: hasAtLeastOneVisibleTrendMetric(trendMetricVisibility),
    updateTrendMetricVisibility
  };
};
