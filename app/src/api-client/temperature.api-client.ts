import { httpClient } from "@/api-client/httpClient";
import type { MeasurementQuery, TemperatureDto } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/temperatures";

export const listTemperatures = async (query: MeasurementQuery): Promise<TemperatureDto[]> => {
  try {
    const { data, error } = await httpClient.GET(PATH, { params: { query } });
    if (error) {
      throw toApiError(error);
    }
    return data;
  } catch (error) {
    throw toApiError(error);
  }
};

export const createTemperature = async (newReading: TemperatureDto): Promise<TemperatureDto> => {
  try {
    const { data, error } = await httpClient.POST(PATH, { body: newReading });
    if (error) {
      throw toApiError(error);
    }
    return data;
  } catch (error) {
    throw toApiError(error);
  }
};

export const deleteTemperatures = async (query: MeasurementQuery): Promise<void> => {
  try {
    const { error } = await httpClient.DELETE(PATH, { params: { query } });
    if (error) {
      throw toApiError(error);
    }
  } catch (error) {
    throw toApiError(error);
  }
};
