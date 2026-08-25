import useSWRMutation from "swr/mutation";

import {
  deleteBatteriesByDateOrTimestamp,
  deleteHumiditiesByDateOrTimestamp,
  deleteTemperaturesByDateOrTimestamp
} from "../lib";
import type { SensorResource } from "../types";

interface DeleteReadingArgs {
  resource: SensorResource;
  registeredAt: string;
  dateOnly: boolean;
}

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
