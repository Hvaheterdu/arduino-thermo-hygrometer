import useSWR from "swr";

import { getHumiditiesByDateOrTimestamp, getHumidityKey } from "@/lib";

type HumidityKey = ReturnType<typeof getHumidityKey>;

export const useHumidityReadings = (registeredAt: string, dateOnly: boolean, enabled: boolean = true) => {
  const key: HumidityKey | null = enabled ? getHumidityKey(registeredAt, dateOnly) : null;
  return useSWR<Awaited<ReturnType<typeof getHumiditiesByDateOrTimestamp>>, Error>(
    key,
    (keyValue: HumidityKey) => getHumiditiesByDateOrTimestamp({ registeredAt: keyValue[1], dateOnly: keyValue[2] }),
    { revalidateOnMount: false, revalidateIfStale: false }
  );
};
