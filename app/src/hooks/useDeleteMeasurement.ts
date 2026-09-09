import { mutate } from "swr";
import useSWRMutation, { type SWRMutationResponse } from "swr/mutation";

import { measurementClients } from "@/api-client/measurementClients";
import type { MeasurementQuery, ResourceKind } from "@/types/measurement.types";
import type { ApiError } from "@/types/problemDetails.types";

export const useDeleteMeasurement = (
  resource: ResourceKind
): SWRMutationResponse<void, ApiError, string, MeasurementQuery> => {
  const fetcher = async (_key: string, { arg: deleteQuery }: { arg: MeasurementQuery }): Promise<void> => {
    await measurementClients[resource].remove(deleteQuery);
    await mutate((key) => Array.isArray(key) && key[0] === "measurements" && key[1] === resource, undefined, {
      revalidate: true
    });
  };

  return useSWRMutation(`delete-${resource}`, fetcher) as SWRMutationResponse<void, ApiError, string, MeasurementQuery>;
};
