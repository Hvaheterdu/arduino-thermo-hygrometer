import type { JSX } from "react";
import { MetricTrendChart } from "../../../features/insights/components/MetricTrendChart";
import type { DailyTrendPoint } from "../../../features/insights/model/insightTypes";
import type { TrendMetricVisibility } from "../model/trendMetricVisibility";

interface TrendChartsSectionProps {
  readonly trendMetricVisibility: TrendMetricVisibility;
  readonly dailyTrendPoints: readonly DailyTrendPoint[];
  readonly hasAtLeastOneVisibleMetric: boolean;
  readonly isLoading: boolean;
}

export const TrendChartsSection = ({
  trendMetricVisibility,
  dailyTrendPoints,
  hasAtLeastOneVisibleMetric,
  isLoading
}: TrendChartsSectionProps): JSX.Element => {
  return (
    <section className="panel metric-charts-grid" aria-label="Trend charts" aria-busy={isLoading}>
      {hasAtLeastOneVisibleMetric ?
        <>
          {trendMetricVisibility.battery ?
            <MetricTrendChart
              title="Battery"
              unit="%"
              points={dailyTrendPoints}
              getValue={(dailyTrendPoint) => dailyTrendPoint.batteryAverage}
              lineClassName="metric-chart-battery"
            />
          : null}
          {trendMetricVisibility.temperature ?
            <MetricTrendChart
              title="Temperature"
              unit="°C"
              points={dailyTrendPoints}
              getValue={(dailyTrendPoint) => dailyTrendPoint.temperatureAverage}
              lineClassName="metric-chart-temperature"
            />
          : null}
          {trendMetricVisibility.humidity ?
            <MetricTrendChart
              title="Humidity"
              unit="%"
              points={dailyTrendPoints}
              getValue={(dailyTrendPoint) => dailyTrendPoint.humidityAverage}
              lineClassName="metric-chart-humidity"
            />
          : null}
        </>
      : <p className="field-help">Enable at least one metric to view charts.</p>}
    </section>
  );
};
