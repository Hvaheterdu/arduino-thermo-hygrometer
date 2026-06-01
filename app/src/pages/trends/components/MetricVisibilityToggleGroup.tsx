import type { JSX } from "react";
import { ToggleButton } from "react-aria-components";
import type { TrendMetricVisibility } from "../model/trendMetricVisibility";

interface MetricVisibilityToggleGroupProps {
  readonly trendMetricVisibility: TrendMetricVisibility;
  readonly onVisibilityChange: (
    metricVisibilityKey: "battery" | "temperature" | "humidity",
    isMetricVisible: boolean
  ) => void;
}

export const MetricVisibilityToggleGroup = ({
  trendMetricVisibility,
  onVisibilityChange
}: MetricVisibilityToggleGroupProps): JSX.Element => {
  return (
    <section className="panel" aria-label="Chart visibility">
      <h2>Visible Metrics</h2>
      <div className="chart-toggle-list" role="group" aria-label="Toggle metrics in charts and tables">
        <ToggleButton
          isSelected={trendMetricVisibility.battery}
          onChange={(isMetricVisible) => {
            onVisibilityChange("battery", isMetricVisible);
          }}
        >
          Battery
        </ToggleButton>
        <ToggleButton
          isSelected={trendMetricVisibility.temperature}
          onChange={(isMetricVisible) => {
            onVisibilityChange("temperature", isMetricVisible);
          }}
        >
          Temperature
        </ToggleButton>
        <ToggleButton
          isSelected={trendMetricVisibility.humidity}
          onChange={(isMetricVisible) => {
            onVisibilityChange("humidity", isMetricVisible);
          }}
        >
          Humidity
        </ToggleButton>
      </div>
    </section>
  );
};
