import useSWRMutation from "swr/mutation";

import { createTemperature } from "@/lib";
import type { TemperatureDto } from "@/types";

type CreateTemperatureArgs = {
  arg: TemperatureDto;
};

export const useCreateTemperature = () =>
  useSWRMutation<TemperatureDto, Error, "create-temperature", TemperatureDto>(
    "create-temperature",
    (_key: "create-temperature", { arg }: CreateTemperatureArgs) => createTemperature(arg)
  );
