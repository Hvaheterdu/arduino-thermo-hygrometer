import useSWRMutation from "swr/mutation";

import { createHumidity } from "../lib";
import type { HumidityDto } from "../types";

export const useCreateHumidity = () =>
  useSWRMutation<HumidityDto, Error, "create-humidity", HumidityDto>("create-humidity", (_key, { arg }) =>
    createHumidity(arg)
  );
