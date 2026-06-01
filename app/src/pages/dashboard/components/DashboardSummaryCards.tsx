import type { JSX } from "react";
import { MetricCard } from "../../../features/readings/components/MetricCard";
import type { DashboardSummaryValues } from "../model/dashboardSummary";

interface DashboardSummaryCardsProps {
  readonly dashboardSummaryValues: DashboardSummaryValues;
}

export const DashboardSummaryCards = ({ dashboardSummaryValues }: DashboardSummaryCardsProps): JSX.Element => {
  return (
    <section className="metrics-grid" aria-label="Latest values">
      <MetricCard
        label="Battery"
        value={dashboardSummaryValues.batteryLatestValue}
        helperText={dashboardSummaryValues.batteryReadingCountDescription}
      />
      <MetricCard
        label="Temperature"
        value={dashboardSummaryValues.temperatureLatestValue}
        helperText={dashboardSummaryValues.temperatureReadingCountDescription}
      />
      <MetricCard
        label="Humidity"
        value={dashboardSummaryValues.humidityLatestValue}
        helperText={dashboardSummaryValues.humidityReadingCountDescription}
      />
    </section>
  );
};
