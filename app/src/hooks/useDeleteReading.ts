import useSWRMutation from "swr/mutation";

import {
  deleteBatteriesByDateOrTimestamp,
  deleteHumiditiesByDateOrTimestamp,
  deleteTemperaturesByDateOrTimestamp
} from "@/lib";
import type { SensorResource } from "@/types";

type DeleteReadingArgs = {
  resource: SensorResource;
  registeredAt: string;
  dateOnly: boolean;
};

type DeleteReadingRequest = {
  arg: DeleteReadingArgs;
};

export const useDeleteReading = () =>
  useSWRMutation<void, Error, "delete-reading", DeleteReadingArgs>(
    "delete-reading",
    async (_key: "delete-reading", { arg }: DeleteReadingRequest): Promise<void> => {
      if (arg.resource === "battery") {
        await deleteBatteriesByDateOrTimestamp(arg);
        return;
      }

      if (arg.resource === "humidity") {
        await deleteHumiditiesByDateOrTimestamp(arg);
        return;
      }

      await deleteTemperaturesByDateOrTimestamp(arg);
    }
  );
