import type { JSX } from "react";

interface DashboardLoadStatusProps {
  readonly isHistoricalReadingsLoading: boolean;
}

export const DashboardLoadStatus = ({ isHistoricalReadingsLoading }: DashboardLoadStatusProps): JSX.Element => {
  return (
    <p className="screen-reader-only" role="status" aria-live="polite">
      {isHistoricalReadingsLoading ? "Loading historical readings." : "Historical readings loaded."}
    </p>
  );
};
