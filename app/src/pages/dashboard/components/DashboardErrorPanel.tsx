import type { JSX } from "react";

interface DashboardErrorPanelProps {
  readonly errorMessage: string;
}

export const DashboardErrorPanel = ({ errorMessage }: DashboardErrorPanelProps): JSX.Element => {
  return (
    <section className="panel panel-error" role="alert">
      <h2>Unable to load readings</h2>
      <p>{errorMessage}</p>
    </section>
  );
};
