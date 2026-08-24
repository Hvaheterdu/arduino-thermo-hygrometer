import useSWRMutation from "swr/mutation";

import { createBattery } from "@/lib";
import type { BatteryDto } from "@/types";

type CreateBatteryArgs = {
  arg: BatteryDto;
};

export const useCreateBattery = () =>
  useSWRMutation<BatteryDto, Error, "create-battery", BatteryDto>(
    "create-battery",
    (_key: "create-battery", { arg }: CreateBatteryArgs) => createBattery(arg)
  );
