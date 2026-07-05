import type { JSX } from "react";
import type { DailyTrendPoint } from "../../../features/insights/model/insightTypes";
import { formatTrendMetricValue } from "../model/formatTrendMetric";
import type { TrendMetricVisibility } from "../model/trendMetricVisibility";

interface TrendDailyAveragesTableProps {
  readonly trendMetricVisibility: TrendMetricVisibility;
  readonly dailyTrendPoints: readonly DailyTrendPoint[];
  readonly failedApiDays: number;
}

export const TrendDailyAveragesTable = ({
  trendMetricVisibility,
  dailyTrendPoints,
  failedApiDays
}: TrendDailyAveragesTableProps): JSX.Element => {
  return (
    <section className="panel panel-table" aria-label="Daily averages">
      <h2>Daily Averages</h2>
      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th scope="col">Day</th>
              {trendMetricVisibility.battery ? <th scope="col">Battery</th> : null}
              {trendMetricVisibility.temperature ? <th scope="col">Temperature</th> : null}
              {trendMetricVisibility.humidity ? <th scope="col">Humidity</th> : null}
              <th scope="col">Samples</th>
            </tr>
          </thead>
          <tbody>
            {dailyTrendPoints.map((dailyTrendPoint) => {
              return (
                <tr key={dailyTrendPoint.dayLabel}>
                  <td>{dailyTrendPoint.dayLabel}</td>
                  {trendMetricVisibility.battery ? (
                    <td>{formatTrendMetricValue(dailyTrendPoint.batteryAverage, "%")}</td>
                  ) : null}
                  {trendMetricVisibility.temperature ? (
                    <td>{formatTrendMetricValue(dailyTrendPoint.temperatureAverage, "°C")}</td>
                  ) : null}
                  {trendMetricVisibility.humidity ? (
                    <td>{formatTrendMetricValue(dailyTrendPoint.humidityAverage, "%")}</td>
                  ) : null}
                  <td>{dailyTrendPoint.sampleCount}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <p className="field-help">Failed API days in window: {failedApiDays}</p>
    </section>
  );
};
