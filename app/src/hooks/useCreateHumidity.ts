import { createHumidity } from "@lib/api/humidity";
import useSWRMutation from "swr/mutation";

import type { HumidityDto } from "@/types/domain";

export const useCreateHumidity = () =>
  useSWRMutation<HumidityDto, Error, "create-humidity", HumidityDto>("create-humidity", (_key, { arg }) =>
    createHumidity(arg)
  );
