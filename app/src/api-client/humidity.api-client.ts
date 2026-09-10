import { httpClient } from "@/api-client/httpClient";
import type { HumidityDto, MeasurementQuery } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/humidities";

export const listHumidities = async (query: MeasurementQuery): Promise<HumidityDto[]> => {
  const { data, error } = await httpClient.GET(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const createHumidity = async (newReading: HumidityDto): Promise<HumidityDto> => {
  const { data, error } = await httpClient.POST(PATH, { body: newReading });
  if (error) {
    throw toApiError(error);
  }

  return data;
};

export const deleteHumidities = async (query: MeasurementQuery): Promise<void> => {
  const { error } = await httpClient.DELETE(PATH, { params: { query } });
  if (error) {
    throw toApiError(error);
  }
};
