import type { JSX } from "react";
import { useMemo, useState } from "react";
import { HistoryFiltersPanel } from "../../features/readings/components/HistoryFiltersPanel";
import { ReadingsTable } from "../../features/readings/components/ReadingsTable";
import { useHistoricalReadings } from "../../features/readings/hooks/useHistoricalReadings";
import { initialFilterDraft, toSubmittedFilters, type FilterDraft } from "../../features/readings/model/historyFilters";
import { DashboardErrorPanel } from "./components/DashboardErrorPanel";
import { DashboardHero } from "./components/DashboardHero";
import { DashboardLoadStatus } from "./components/DashboardLoadStatus";
import { DashboardSummaryCards } from "./components/DashboardSummaryCards";
import { buildDashboardSummaryValues } from "./model/dashboardSummary";

export const DashboardPage = (): JSX.Element => {
  const [draftFilters, setDraftFilters] = useState<FilterDraft>(initialFilterDraft);
  const [submittedFilters, setSubmittedFilters] = useState(() => toSubmittedFilters(initialFilterDraft));

  const readingsQuery = useHistoricalReadings(submittedFilters);

  const dashboardSummaryValues = useMemo(() => {
    return buildDashboardSummaryValues(readingsQuery.data);
  }, [readingsQuery.data]);

  return (
    <>
      <a className="skip-link" href="#historical-results">
        Skip to results
      </a>

      <main className="dashboard-shell" aria-busy={readingsQuery.isFetching}>
        <DashboardHero />

        <DashboardSummaryCards dashboardSummaryValues={dashboardSummaryValues} />

        <HistoryFiltersPanel
          draft={draftFilters}
          onDraftChange={setDraftFilters}
          onSubmit={() => {
            setSubmittedFilters(toSubmittedFilters(draftFilters));
          }}
          isLoading={readingsQuery.isFetching}
        />

        <DashboardLoadStatus isHistoricalReadingsLoading={readingsQuery.isFetching} />

        {readingsQuery.isError ? <DashboardErrorPanel errorMessage={readingsQuery.error.message} /> : null}

        <ReadingsTable
          batteryReadings={readingsQuery.data?.battery ?? []}
          temperatureReadings={readingsQuery.data?.temperature ?? []}
          humidityReadings={readingsQuery.data?.humidity ?? []}
        />
      </main>
    </>
  );
};
