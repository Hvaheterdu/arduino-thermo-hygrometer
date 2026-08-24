import useSWR from "swr";

import { getBatteriesByDateOrTimestamp, getBatteryKey } from "@/lib";

type BatteryKey = ReturnType<typeof getBatteryKey>;

export const useBatteryReadings = (registeredAt: string, dateOnly: boolean, enabled: boolean = true) => {
  const key: BatteryKey | null = enabled ? getBatteryKey(registeredAt, dateOnly) : null;
  return useSWR<Awaited<ReturnType<typeof getBatteriesByDateOrTimestamp>>, Error>(
    key,
    (keyValue: BatteryKey) => getBatteriesByDateOrTimestamp({ registeredAt: keyValue[1], dateOnly: keyValue[2] }),
    { revalidateOnMount: false, revalidateIfStale: false }
  );
};
