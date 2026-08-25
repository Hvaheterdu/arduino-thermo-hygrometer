import useSWRMutation from "swr/mutation";

import { createTemperature } from "../lib";
import type { TemperatureDto } from "../types";

export const useCreateTemperature = () =>
  useSWRMutation<TemperatureDto, Error, "create-temperature", TemperatureDto>("create-temperature", (_key, { arg }) =>
    createTemperature(arg)
  );
