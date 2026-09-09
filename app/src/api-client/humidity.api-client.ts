import { httpClient } from "@/api-client/httpClient";
import type { HumidityDto, MeasurementQuery } from "@/types/measurement.types";
import { toApiError } from "@/utils/problemDetails.util";

const PATH = "/api/v1/humidities";

export const listHumidities = async (query: MeasurementQuery): Promise<HumidityDto[]> => {
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

export const createHumidity = async (newReading: HumidityDto): Promise<HumidityDto> => {
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

export const deleteHumidities = async (query: MeasurementQuery): Promise<void> => {
  try {
    const { error } = await httpClient.DELETE(PATH, { params: { query } });
    if (error) {
      throw toApiError(error);
    }
  } catch (error) {
    throw toApiError(error);
  }
};
