import { useQuery, type UseQueryResult } from "@tanstack/react-query";
import { defaultReadingsRepository } from "../data/readingsRepository";
import { applySearchFilters } from "../model/filterReadings";
import type { SubmittedFilters } from "../model/historyFilters";
import type { HistoricalReadings } from "../model/readingTypes";
import { getHistoricalReadings } from "../services/getHistoricalReadings";

const emptyReadings: HistoricalReadings = {
  battery: [],
  temperature: [],
  humidity: []
};

export const useHistoricalReadings = (submittedFilters: SubmittedFilters): UseQueryResult<HistoricalReadings> => {
  return useQuery({
    queryKey: ["historical-readings", submittedFilters],
    queryFn: async () => {
      const rawReadings = await getHistoricalReadings(defaultReadingsRepository, submittedFilters.request);
      return applySearchFilters(rawReadings, submittedFilters.searchFilters);
    },
    placeholderData: emptyReadings,
    staleTime: 60_000
  });
};
