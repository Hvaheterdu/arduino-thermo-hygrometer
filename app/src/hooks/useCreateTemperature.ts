import { createTemperature } from "@lib/api/temperature";
import useSWRMutation from "swr/mutation";

import type { TemperatureDto } from "@/types/domain";

export const useCreateTemperature = () =>
  useSWRMutation<TemperatureDto, Error, "create-temperature", TemperatureDto>("create-temperature", (_key, { arg }) =>
    createTemperature(arg)
  );
