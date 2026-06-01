import type { JSX } from "react";
import type { TrendsInsights } from "../../../features/insights/model/insightTypes";
import { formatTrendMetricDeltaValue, formatTrendMetricValue } from "../model/formatTrendMetric";
import type { TrendMetricVisibility } from "../model/trendMetricVisibility";

interface TrendSummaryCardsProps {
  readonly trendsInsights: TrendsInsights | undefined;
  readonly trendMetricVisibility: TrendMetricVisibility;
  readonly isLoading: boolean;
}

export const TrendSummaryCards = ({
  trendsInsights,
  trendMetricVisibility,
  isLoading
}: TrendSummaryCardsProps): JSX.Element => {
  return (
    <section className="panel trends-grid" aria-label="Trend modules" aria-busy={isLoading}>
      {trendMetricVisibility.battery ?
        <article className="trend-card">
          <h2>Battery Direction</h2>
          <p>{formatTrendMetricValue(trendsInsights?.battery.currentValue, "%")}</p>
          <p className="trend-subtext">
            7-day delta: {formatTrendMetricDeltaValue(trendsInsights?.battery.deltaValue, "%")}
          </p>
        </article>
      : null}

      {trendMetricVisibility.temperature ?
        <article className="trend-card">
          <h2>Temperature Direction</h2>
          <p>{formatTrendMetricValue(trendsInsights?.temperature.currentValue, "°C")}</p>
          <p className="trend-subtext">
            7-day delta: {formatTrendMetricDeltaValue(trendsInsights?.temperature.deltaValue, "°C")}
          </p>
        </article>
      : null}

      {trendMetricVisibility.humidity ?
        <article className="trend-card">
          <h2>Humidity Direction</h2>
          <p>{formatTrendMetricValue(trendsInsights?.humidity.currentValue, "%")}</p>
          <p className="trend-subtext">
            7-day delta: {formatTrendMetricDeltaValue(trendsInsights?.humidity.deltaValue, "%")}
          </p>
        </article>
      : null}
    </section>
  );
};
