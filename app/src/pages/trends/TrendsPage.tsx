import type { JSX } from "react";
import { useTrendsInsights } from "../../features/insights/hooks/useTrendsInsights";
import { MetricVisibilityToggleGroup } from "./components/MetricVisibilityToggleGroup";
import { TrendChartsSection } from "./components/TrendChartsSection";
import { TrendDailyAveragesTable } from "./components/TrendDailyAveragesTable";
import { TrendSummaryCards } from "./components/TrendSummaryCards";
import { useTrendMetricVisibility } from "./hooks/useTrendMetricVisibility";

export const TrendsPage = (): JSX.Element => {
  const trendsQuery = useTrendsInsights();
  const { trendMetricVisibility, hasAtLeastOneVisibleMetric, updateTrendMetricVisibility } = useTrendMetricVisibility();

  return (
    <main className="dashboard-shell" id="trends-content">
      <section className="hero">
        <p className="hero-badge">Trend Analysis</p>
        <h1>Telemetry Trends</h1>
        <p>
          Review seven-day trend direction for each metric based on daily averages derived from historical readings.
        </p>
      </section>

      {trendsQuery.isError ?
        <section className="panel panel-error" role="alert">
          <h2>Unable to load trends</h2>
          <p>{trendsQuery.error.message}</p>
        </section>
      : null}

      <TrendSummaryCards
        trendsInsights={trendsQuery.data}
        trendMetricVisibility={trendMetricVisibility}
        isLoading={trendsQuery.isFetching}
      />

      <MetricVisibilityToggleGroup
        trendMetricVisibility={trendMetricVisibility}
        onVisibilityChange={updateTrendMetricVisibility}
      />

      <TrendChartsSection
        trendMetricVisibility={trendMetricVisibility}
        dailyTrendPoints={trendsQuery.data?.points ?? []}
        hasAtLeastOneVisibleMetric={hasAtLeastOneVisibleMetric}
        isLoading={trendsQuery.isFetching}
      />

      <TrendDailyAveragesTable
        trendMetricVisibility={trendMetricVisibility}
        dailyTrendPoints={trendsQuery.data?.points ?? []}
        failedApiDays={trendsQuery.data?.failedDays ?? 0}
      />
    </main>
  );
};
