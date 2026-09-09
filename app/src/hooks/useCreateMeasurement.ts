import { mutate } from "swr";
import useSWRMutation, { type SWRMutationResponse } from "swr/mutation";

import { measurementClients } from "@/api-client/measurementClients";
import type { MeasurementDto, ResourceKind } from "@/types/measurement.types";
import type { ApiError } from "@/types/problemDetails.types";

export const useCreateMeasurement = (
  resource: ResourceKind
): SWRMutationResponse<MeasurementDto, ApiError, string, MeasurementDto> => {
  const fetcher = async (_key: string, { arg: newReading }: { arg: MeasurementDto }): Promise<MeasurementDto> => {
    const created = await measurementClients[resource].create(newReading as never);
    await mutate((key) => Array.isArray(key) && key[0] === "measurements" && key[1] === resource, undefined, {
      revalidate: true
    });
    return created;
  };

  return useSWRMutation(`create-${resource}`, fetcher) as SWRMutationResponse<
    MeasurementDto,
    ApiError,
    string,
    MeasurementDto
  >;
};
