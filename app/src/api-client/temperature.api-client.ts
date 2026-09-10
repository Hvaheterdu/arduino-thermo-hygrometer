import { httpClient } from "@/api-client/httpClient";
import type { MeasurementQuery, TemperatureDto } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/temperatures";

export const listTemperatures = async (query: MeasurementQuery): Promise<TemperatureDto[]> => {
  const { data, error } = await httpClient.GET(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const createTemperature = async (newReading: TemperatureDto): Promise<TemperatureDto> => {
  const { data, error } = await httpClient.POST(PATH, { body: newReading });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const deleteTemperatures = async (query: MeasurementQuery): Promise<void> => {
  const { error } = await httpClient.DELETE(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }
};
