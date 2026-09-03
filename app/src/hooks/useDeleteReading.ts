import { deleteBatteriesByDateOrTimestamp } from "@lib/api/battery";
import { deleteHumiditiesByDateOrTimestamp } from "@lib/api/humidity";
import { deleteTemperaturesByDateOrTimestamp } from "@lib/api/temperature";
import useSWRMutation from "swr/mutation";

import type { SensorResource } from "@/types/sensor";

type DeleteReadingArgs = {
  resource: SensorResource;
  registeredAt: string;
  dateOnly: boolean;
};

export const useDeleteReading = () =>
  useSWRMutation<void, Error, "delete-reading", DeleteReadingArgs>("delete-reading", async (_key, { arg }) => {
    if (arg.resource === "battery") {
      await deleteBatteriesByDateOrTimestamp(arg);
      return;
    }

    if (arg.resource === "humidity") {
      await deleteHumiditiesByDateOrTimestamp(arg);
      return;
    }

    await deleteTemperaturesByDateOrTimestamp(arg);
  });
