import useSWRMutation from "swr/mutation";

import { createBattery } from "../lib";
import type { BatteryDto } from "../types";

export const useCreateBattery = () =>
  useSWRMutation<BatteryDto, Error, "create-battery", BatteryDto>("create-battery", (_key, { arg }) =>
    createBattery(arg)
  );
