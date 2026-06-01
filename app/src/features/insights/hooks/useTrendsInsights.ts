import { useQuery, type UseQueryResult } from "@tanstack/react-query";
import type { TrendsInsights } from "../model/insightTypes";
import { buildTrendsInsights } from "../services/buildInsights";
import { getDailySnapshots } from "../services/getDailySnapshots";

export const useTrendsInsights = (days = 7): UseQueryResult<TrendsInsights> => {
  return useQuery({
    queryKey: ["trends-insights", days],
    queryFn: async () => {
      const snapshots = await getDailySnapshots(days);
      return buildTrendsInsights(snapshots);
    },
    staleTime: 60_000
  });
};
