import { useQuery, type UseQueryResult } from "@tanstack/react-query";
import type { SensorHealthInsights } from "../model/insightTypes";
import { buildSensorHealthInsights } from "../services/buildInsights";
import { getDailySnapshots } from "../services/getDailySnapshots";

export const useSensorHealthInsights = (days = 7): UseQueryResult<SensorHealthInsights> => {
  return useQuery({
    queryKey: ["sensor-health-insights", days],
    queryFn: async () => {
      const snapshots = await getDailySnapshots(days);
      return buildSensorHealthInsights(snapshots);
    },
    staleTime: 60_000
  });
};
