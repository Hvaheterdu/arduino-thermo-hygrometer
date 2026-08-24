import useSWR from "swr";

import { getTemperatureKey, getTemperaturesByDateOrTimestamp } from "@/lib";

type TemperatureKey = ReturnType<typeof getTemperatureKey>;

export const useTemperatureReadings = (registeredAt: string, dateOnly: boolean, enabled: boolean = true) => {
  const key: TemperatureKey | null = enabled ? getTemperatureKey(registeredAt, dateOnly) : null;
  return useSWR<Awaited<ReturnType<typeof getTemperaturesByDateOrTimestamp>>, Error>(
    key,
    (keyValue: TemperatureKey) => getTemperaturesByDateOrTimestamp({
      registeredAt: keyValue[1],
      dateOnly: keyValue[2]
    }),
    { revalidateOnMount: false, revalidateIfStale: false }
  );
};
