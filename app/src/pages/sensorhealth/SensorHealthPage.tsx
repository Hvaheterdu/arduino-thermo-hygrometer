import type { JSX } from "react";
import { useSensorHealthInsights } from "../../features/insights/hooks/useSensorHealthInsights";
import { formatRegisteredAt } from "../../features/readings/model/formatters";

const valueOrFallback = (value: string | number | null, fallback: string): string => {
  if (value === null) {
    return fallback;
  }

  return String(value);
};

export const SensorHealthPage = (): JSX.Element => {
  const healthQuery = useSensorHealthInsights();

  return (
    <main className="dashboard-shell" id="sensor-health-content">
      <section className="hero">
        <p className="hero-badge">Operational Overview</p>
        <h1>Sensor Health</h1>
        <p>Sensor health is derived from a rolling seven-day window of historical API readings.</p>
      </section>

      {healthQuery.isError ? (
        <section className="panel panel-error" role="alert">
          <h2>Unable to load sensor health</h2>
          <p>{healthQuery.error.message}</p>
        </section>
      ) : null}

      <section className="panel health-grid" aria-label="Sensor health modules" aria-busy={healthQuery.isFetching}>
        <article className="health-card">
          <h2>Data Freshness</h2>
          <p>
            {healthQuery.data?.latestRegisteredAt
              ? formatRegisteredAt(healthQuery.data.latestRegisteredAt)
              : "No timestamp available"}
          </p>
          <p className="trend-subtext">
            Age: {valueOrFallback(healthQuery.data?.minutesSinceLatest ?? null, "Unknown")} minutes
          </p>
        </article>

        <article className="health-card">
          <h2>Coverage</h2>
          <p>{valueOrFallback(healthQuery.data?.coveragePercent ?? null, "0")}% of last 7 days</p>
          <p className="trend-subtext">
            Empty successful days: {valueOrFallback(healthQuery.data?.emptyDays ?? null, "Unknown")}
          </p>
        </article>

        <article className="health-card">
          <h2>Connectivity</h2>
          <p>{valueOrFallback(healthQuery.data?.apiReliabilityPercent ?? null, "0")}% successful API days</p>
          <p className="trend-subtext">Failed days: {valueOrFallback(healthQuery.data?.failedDays ?? null, "0")}</p>
        </article>
      </section>
    </main>
  );
};
