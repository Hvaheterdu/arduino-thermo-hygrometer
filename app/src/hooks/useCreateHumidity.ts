import useSWRMutation from "swr/mutation";

import { createHumidity } from "@/lib";
import type { HumidityDto } from "@/types";

type CreateHumidityArgs = {
  arg: HumidityDto;
};

export const useCreateHumidity = () =>
  useSWRMutation<HumidityDto, Error, "create-humidity", HumidityDto>(
    "create-humidity",
    (_key: "create-humidity", { arg }: CreateHumidityArgs) => createHumidity(arg)
  );
