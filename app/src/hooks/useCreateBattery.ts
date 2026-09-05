import { createBattery } from "@lib/api/battery";
import useSWRMutation from "swr/mutation";

import type { BatteryDto } from "@/types/domain";

export const useCreateBattery = () =>
  useSWRMutation<BatteryDto, Error, "create-battery", BatteryDto>("create-battery", (_key, { arg }) =>
    createBattery(arg)
  );
