import type { JSX } from "react";

export const DashboardHero = (): JSX.Element => {
  return (
    <section className="hero">
      <p className="hero-badge">Arduino Thermo Hygrometer</p>
      <h1>Historical Climate Telemetry</h1>
      <p>
        Explore historical battery, temperature, and humidity measurements with an API-first frontend that is prepared
        for generated OpenAPI TypeScript types.
      </p>
    </section>
  );
};
