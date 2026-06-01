import type { JSX } from "react";

interface MetricCardProps {
  readonly label: string;
  readonly value: string;
  readonly helperText: string;
}

export const MetricCard = ({ label, value, helperText }: MetricCardProps): JSX.Element => {
  return (
    <article className="metric-card" aria-label={label}>
      <p className="metric-label">{label}</p>
      <p className="metric-value">{value}</p>
      <p className="metric-helper">{helperText}</p>
    </article>
  );
};
