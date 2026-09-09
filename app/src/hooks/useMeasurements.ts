import useSWR, { type SWRResponse } from "swr";

import { measurementClients } from "@/api-client/measurementClients";
import type { MeasurementDto, MeasurementQuery, ResourceKind } from "@/types/measurement.types";
import type { ApiError } from "@/types/problemDetails.types";

export const measurementKey = (resource: ResourceKind, query: MeasurementQuery) =>
  ["measurements", resource, query.registeredAt, query.dateOnly] as const;

export const useMeasurements = (
  resource: ResourceKind,
  query: MeasurementQuery
): SWRResponse<MeasurementDto[], ApiError> => {
  const fetcher = (): Promise<MeasurementDto[]> => measurementClients[resource].list(query as never);

  return useSWR(measurementKey(resource, query), fetcher) as SWRResponse<MeasurementDto[], ApiError>;
};
